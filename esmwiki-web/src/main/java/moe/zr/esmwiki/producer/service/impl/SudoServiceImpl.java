package moe.zr.esmwiki.producer.service.impl;

import moe.zr.esmwiki.producer.config.QuickReplyConfig;
import moe.zr.esmwiki.producer.repository.BindUserProfileRepository;
import moe.zr.esmwiki.producer.repository.PointRankingRepository;
import moe.zr.esmwiki.producer.util.ReplyUtils;
import moe.zr.pojo.BindUserProfile;
import moe.zr.pojo.PointRanking;
import moe.zr.qqbot.entry.IMessageQuickReply;
import moe.zr.qqbot.entry.Message;
import moe.zr.service.SudoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class SudoServiceImpl implements SudoService {
    private final Map<String, IMessageQuickReply> messageHandlerMap;
    final
    private ReplyUtils replyUtils;

    public SudoServiceImpl(QuickReplyConfig quickReplyConfig, ReplyUtils replyUtils) {
        messageHandlerMap = quickReplyConfig.getMessageHandlerMap();
        this.replyUtils = replyUtils;
    }

    public String sendGroupMessage(String message) {
        replyUtils.sendGroupPostingMessage(message);
        return "ok";
    }

    @Autowired
    BindUserProfileRepository bindUserProfileRepository;
    @Autowired
    PointRankingRepository pointRankingRepository;

    private void cleanCard(Long groupId) {
        List<Long> groupMemberList = replyUtils.getGroupMemberList(groupId);
        groupMemberList.forEach(userId -> replyUtils.renameGroupCard(groupId, userId, null));
        replyUtils.sendMessage("清除名片成功", groupId);
    }

    private void renameCard(Long groupId) {
        AtomicInteger count = new AtomicInteger();
        List<Long> groupMemberList = replyUtils.getGroupMemberList(groupId);
        List<BindUserProfile> profiles = bindUserProfileRepository.findBindUserProfilesByQqNumberIn(groupMemberList);
        List<PointRanking> allByUserIdIn = pointRankingRepository.findAllByUserIdIn(profiles.stream().map(BindUserProfile::getUserId).collect(Collectors.toList()));
        allByUserIdIn.stream().filter(pointRanking -> pointRanking.getRank() < 100000).forEach(
                pointRanking -> {
                    String rank = pointRanking.getRank().toString();
                    StringBuilder stringBuilder = new StringBuilder(rank);
                    while (stringBuilder.length() < 5) {
                        stringBuilder.insert(0, "0");
                    }
                    Optional<BindUserProfile> first = profiles.stream().filter(profile -> profile.getUserId().equals(pointRanking.getUserId())).findFirst();
                    first.ifPresent(profile -> {
                        count.getAndIncrement();
                        replyUtils.renameGroupCard(groupId, profile.getQqNumber(), stringBuilder.toString());
                    });
                }
        );
        replyUtils.sendMessage("成功批量改名" + count + "人", groupId);
    }

    private String handlePermissionCommand(Message message) {
        Long groupId = message.getGroupId();
        Long selfId = message.getSelfId();
        String permission = replyUtils.getPermission(groupId, selfId);
        if (!"admin".equals(permission)) {
            return "No Permission....";
        }
        String[] s = message.getRawMessage().split(" ");
        String command = s[1];
        switch (command) {
            case "!renameCard":
                renameCard(groupId);
                break;
            case "!cleanCard":
                cleanCard(groupId);
                break;
        }
        return null;
    }

    @Override
    public String onMessage(String[] str) {
        int length = str.length;
        if (length == 1) {
            return "喵呜";
        }
        String command = str[1];
        if (command.equals(".sendGroup")) {
            return sendGroupMessage(str[2]);
        }
        IMessageQuickReply iMessageQuickReply = messageHandlerMap.get(command);
        if (iMessageQuickReply == null) {
            return "没有找到对应指令";
        }

        String[] strings = new String[10];
        System.arraycopy(str, 1, strings, 0, str.length - 1);
        strings = Arrays.copyOf(strings, str.length - 1);
        System.out.println(Arrays.toString(strings));
        return iMessageQuickReply.onMessage(strings);
    }

    @Override
    public String onMessage(Message message) {
        Long groupId = message.getGroupId();
        Long userId = message.getUserId();
        if (groupId == 773891409 || userId == 732713726 || userId.equals(message.getSelfId())) {
            String[] s = message.getRawMessage().split(" ");
            String command = s[1];
            if (command.startsWith("!")) {
                return handlePermissionCommand(message);
            }
            return onMessage(s);
        }
        return "斑：\n" +
                "R*der Kick！";
    }

    @Override
    public String commandPrefix() {
        return "/sudo";
    }
}
