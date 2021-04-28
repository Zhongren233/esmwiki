package moe.zr.esmwiki.producer.service.impl;

import moe.zr.esmwiki.producer.repository.StrategyRepository;
import moe.zr.pojo.Strategy;
import moe.zr.qqbot.entry.IMessageQuickReply;
import moe.zr.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class StrategyServiceImpl implements StrategyService, IMessageQuickReply {

    private final StrategyRepository strategyRepository;

    public StrategyServiceImpl(StrategyRepository strategyRepository) {
        this.strategyRepository = strategyRepository;
    }

    public String findDirectory() {
        List<Strategy> byHideFalse = strategyRepository.findByHideFalse();
        HashSet<String> strings = new HashSet<>();
        byHideFalse.forEach(strategy -> strings.add(strategy.getPrefix()));
        return "目录:\n" + strings;
    }

    public String findStrategy(String prefix) {
        Optional<Strategy> byPrefix = strategyRepository.findByPrefix(prefix);
        if (byPrefix.isPresent()) {
            Strategy strategy = byPrefix.get();
            if (strategy.getIsGroup()) {
                return "本功能由 月更攻略组 提供支持，微博地址https://weibo.com/u/7514328254。\n" + strategy.getMessage();
            } else {
                return strategy.getMessage();
            }
        } else {
            return "没有找到相关信息，呜呜";
        }
    }

    @Override
    public String onMessage(String[] str) {
        if (str.length == 2) {
            String prefix = str[1];
            if (prefix.equals("目录")) {
                return findDirectory();
            }
            return findStrategy(prefix);
        }
        return null;
    }

    @Override
    public String commandPrefix() {
        return "查";
    }
}
