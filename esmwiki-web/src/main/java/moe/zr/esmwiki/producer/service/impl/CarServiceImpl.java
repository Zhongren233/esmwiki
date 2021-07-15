package moe.zr.esmwiki.producer.service.impl;

import moe.zr.esmwiki.producer.repository.CarRepository;
import moe.zr.pojo.Car;
import moe.zr.qqbot.entry.IMessageQuickReply;
import moe.zr.qqbot.entry.Message;
import moe.zr.service.CarService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CarServiceImpl implements CarService, IMessageQuickReply {
    final
    CarRepository carRepository;
    final
    StringRedisTemplate stringRedisTemplate;
    private static final String cache = "car::";

    public CarServiceImpl(CarRepository carRepository, StringRedisTemplate stringRedisTemplate) {
        this.carRepository = carRepository;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * @return redis不沉溺在数据时返回true
     */
    public boolean checkFlag(String key) {
        String s = stringRedisTemplate.opsForValue().get(key);
        System.out.println(s);
        return s != null;
    }

    public String startCar(Message message) {
        Long userId = message.getUserId();
        String key = cache + userId;
        if (checkFlag(key)) {
            return "刚才你已经发过车了，休息休息再发吧";
        }
        String rawMessage = message.getRawMessage();
        if (rawMessage.contains("[CQ:")) {
            return "是不是有什么不该出现的东西呢";
        }
        String text = rawMessage.replace("发车", "").trim();
        if (text.length() > 60) {
            return "车牌信息好长，根本记不住呢";
        }
        Car car = new Car();
        car.setText(text);
        car.setQqNumber(userId);
        car.setTime(message.getTime());
        carRepository.insert(car);
        stringRedisTemplate.opsForValue().set(key, String.valueOf(userId), 30, TimeUnit.SECONDS);
        return "发车成功";

    }

    private static final String carFormat = "{0} ({1}秒前)\n";

    public String findCar(Message message) {
        Long time = message.getTime() - 5 * 60;
        String rawMessage = message.getRawMessage();
        String text =
                rawMessage.replace("有车吗", "")
                .replace("车牌列表","")
                        .replace("ycm", "")
                        .trim();
        List<Car> cars = carRepository.findByTimeAfterAndTextContainsOrderByTimeDesc(time, text);
        if (cars.isEmpty()) {
            return "没有车车，呜呜。";
        }
        StringBuilder stringBuilder = new StringBuilder("当前车牌：\n");
        for (Car car : cars) {
            stringBuilder.append(
                    MessageFormat.format(carFormat, car.getText(), message.getTime() - car.getTime())
            );
        }
        return stringBuilder.toString();
    }

    @Override
    public String onMessage(String[] str) {
        return null;
    }

    @Override
    public String onMessage(Message message) {
        String rawMessage = message.getRawMessage();
        if (rawMessage.startsWith("发车")) {
            return startCar(message);
        }
        return findCar(message);
    }

    @Override
    public String commandPrefix() {
        return "有车吗|ycm|车牌列表|发车";
    }
}
