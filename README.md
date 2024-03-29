NEW VERSION IN
https://github.com/Zhongren233/aira-core

## 简介

`偶像梦幻祭2`的相关QQ bot项目

Bot交流群:939482111

### 关于Aira

Aira机器人只允许使用于ES群。若需要请联系开发者。

头像By：`鹤梅路可乐提供商`

### 指令列表

`/pr`  `/sr` `/statme` `/bind` `今日运势` `/birthday`

#### /pr

> 本指令仅可在活动期间+活动结束统计后使用

- `/pr now` 返回当前活动pt榜档线
- `/pr batch` 返回当前活动拿卡人数
- `/pr batch tour` 返回当前活动(巡演)拿卡人数

#### /sr

> 本指令仅可在活动期间+活动结束统计后使用

返回当前活动歌曲榜档线

#### /statme

> 本指令需要在/bind绑定游戏账号后后使用

返回绑定游戏账号的实时pt与排名

注意：活动歌曲排名不会实时更新

#### /bind

> 本指令仅可在活动期间+活动结束统计后使用
>
> 推荐私聊使用
> 
> 推荐使用默认绑定方式

绑定游戏账号，建议在活动开始一个小时后进行绑定。

`/bind {游戏昵称}` 后会获得随机五位数字，将游戏昵称改为随机五位数字后，再次输入`/bind {游戏昵称}`以进行绑定。

![img.png](docs/assets/bind_1.png)

<details>
<summary>昵称重复怎么办</summary>
若游戏昵称重名，可根据实际情况按照id进行绑定。

![img.png](docs/assets/bind_2.png)

按照point和rank筛选出自己的userId，之后使用`/bind {userId} -id`进行绑定。

```
    /bind 70937797 -id
    游戏内改名
    /bind 70937797 -id    
```

</details>

<details>
<summary>通过好友接口绑定</summary>

`/bind 游戏昵称 -friend` 后会获得随机五位数字，将游戏昵称改为随机五位数字后，输入`/bind 随机五位数字 -friend`

</details>


> `{}` 为占位符，请在输入指令时不要输入。
>
> userId不是uid！

> 若出现null或未找到记录，请下一个小时进行绑定

#### 今日运势

返回今日运势。

注意：Aira不对任何由于今日运势导致的抽卡事故进行保证！

#### /birthday

`/birthday` 返回角色生日倒计时

#### /event

`/event` 返回活动倒计时

### Q&A

#### 如何邀请Aira入群？

邀请机器人请加入群(939482111)，联系群主邀请Aira入群。

#### Aira不动了怎么办

在上面的群里面at群主，并说明问题。

#### 我有一个新feature，能否实现？

在群内at开发者，说明想要实现的特性，看开发者心情。
