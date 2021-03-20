package moe.zr.esmwiki.producer.service.impl;

import moe.zr.esmwiki.producer.repository.BindUserProfileRepository;
import moe.zr.pojo.BindUserProfile;
import moe.zr.qqbot.entry.IMessageQuickReply;
import moe.zr.qqbot.entry.Message;
import moe.zr.service.StalkerService;
import org.springframework.stereotype.Service;

@Service
public class StatMeServiceImpl implements IMessageQuickReply {
    final
    StalkerService stalkerService;
    final
    BindUserProfileRepository bindUserProfileRepository;

    public StatMeServiceImpl(StalkerService stalkerService, BindUserProfileRepository bindUserProfileRepository) {
        this.stalkerService = stalkerService;
        this.bindUserProfileRepository = bindUserProfileRepository;
    }

    /**
     * 看起来这玩意没啥用
     * 就放这了
     */
    @Override
    public String onMessage(String[] str) {
        return null;
    }

    @Override
    public String onMessage(Message message) {
        Long qqNumber = message.getUserId();
        BindUserProfile bindUserProfile = bindUserProfileRepository.findBindUserProfileByQqNumber(qqNumber);
        if (bindUserProfile == null) {
            return "你还没有绑定哦，请使用 /bind 进行绑定";
        }
        Integer userId = bindUserProfile.getUserId();
        return stalkerService.getReturnString(userId);
    }

    @Override
    public String commandPrefix() {
        return "/statme";
    }
}
