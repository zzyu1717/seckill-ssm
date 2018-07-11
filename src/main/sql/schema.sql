-- 数据库初始化脚本

CREATE DATABASE seckill;

use seckill;

-- 秒杀库存表
CREATE TABLE stock (
`stock_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
`name` VARCHAR(120) NOT NULL  COMMENT '商品名称',
`number` INT NOT NULL COMMENT '库存数量',
`start_time` TIMESTAMP NOT NULL COMMENT '秒杀开始时间',
`end_time` TIMESTAMP  NOT NULL COMMENT '秒杀结束时间',
`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (stock_id),
KEY idx_start_time(start_time),
KEY idx_end_time(end_time),
KEY idx_create_time(create_time)


) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';


INSERT INTO  stock(name,number,start_time,end_time)
VALUES
  ('1000元秒杀Iphone8',100,'2018-07-11 00:00:00','2018-07-11 00:00:00'),
('3000元秒杀Ipad',50,'2018-07-11 00:00:00','2018-07-11 00:00:00'),
('500元秒杀小米6',200,'2018-07-11 00:00:00','2018-07-11 00:00:00'),
('200元秒杀红米7',100,'2018-07-11 00:00:00','2018-07-11 00:00:00');


-- 秒杀成功明细表
-- 用户登录认证相关的信息

CREATE TABLE success_killed (
  `seckill_id` bigint NOT NULL COMMENT '秒杀商品id',
  `user_phone` bigint NOT NULL COMMENT '用户手机号',
  `state` tinyint NOT NULL DEFAULT -1 COMMENT '状态表示：-1，无效；0，成功；1，已付款；2，已发货',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP  COMMENT '创建时间',
  PRIMARY KEY (seckill_id,user_phone) ,
  KEY idx_create_time(create_time)
)ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';