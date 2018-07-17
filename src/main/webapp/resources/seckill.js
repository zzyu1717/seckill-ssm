//存放主要交互逻辑js代码
// javascript 模块化
var seckill = {
    
    // 封装秒杀相关ajax的url
    URL : {
        now : function () {
            return '/seckill/time/now';
        },

        exposer:function (seckillId) {
            return '/seckill/'+seckillId+'/exposer';
        },

        execution: function (seckillId,md5) {
            return '/seckill/'+seckillId+'/'+md5+'/execution'
        }
    },

    //验证手机号
    validatePhone : function (phone) {
        if (phone && phone.length==11 && !isNaN(phone)) {
            return true;
        }
        return false;
    },

    handleSeckill : function (seckillId,node) {
      // 处理秒杀逻辑
        /*获取秒杀地址，控制显示逻辑，执行秒杀*/
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>')

        $.post(seckill.URL.exposer(seckillId),{},function (result) {

            if (result && result['success']) {

                var exposer = result['data'];
                if (exposer['exposed']) {
                    // 开启秒杀

                    /**
                     * 获取秒杀地址
                     */
                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId,md5);

                    /**
                     * 显示按钮 one:只点击一次,减轻服务器压力
                     */
                    $('#killBtn').one('click',function () {
                        // 执行秒杀请求

                        /*1、先禁用按钮*/
                        $(this).addClass('disabled');

                        /*2、发送秒杀请求执行秒杀*/
                        $.post(killUrl,{}, function (result) {
                            if (result && result['success']) {
                                var killResult = result['data'];
                               // var state = killResult['state'];
                                var stateInfo = killResult == null ? result['error'] : killResult['stateInfo'];

                                /*3、显示秒杀结果*/
                                node.html('<span class="label label-success">'+stateInfo+'</span>')
                            }
                        });
                    });
                    
                    node.show();

                } else {
                    // 未开启秒杀
                    var start = exposer['start'];
                    var end = exposer['end'];
                    var now = exposer['now'];
                    // 重新进入计时逻辑
                    seckill.countdown(seckillId,now,start,end);
                }

            } else {
                console.log("result : " + result);
            }
        })

    },

    countdown : function (seckillId,nowTime,startTime,endTime) {

        var seckillBox = $('#seckillBox');

        if (nowTime > endTime) {
            // 秒杀结束
            seckillBox.html('秒杀已结束');
        } else if (nowTime < startTime) {
            //秒杀未开启，计时事件绑定
            var killTime = new Date(startTime+1000);
            //jquery countdown插件
            seckillBox.countdown(killTime,function(event) {
                // 时间格式
                var format = event.strftime('秒杀倒计时：%D天  %H时  %M分  %S秒');
                seckillBox.html(format);
                /*时间完成后回掉事件*/
            }).on('finish.countdown',function () {
                /*获取秒杀地址，控制实现逻辑，执行秒杀*/
                seckill.handleSeckill(seckillId,seckillBox);
            });

        } else {
            // 秒杀开始
            seckill.handleSeckill(seckillId,seckillBox);
        }
    },
    
    // 详情页秒杀逻辑
    detail : {
        
        // 详情页初始化
        init : function (params) {
            // 手机验证和登录，计时交互
            // 规划我们的交互流程

            // 在cookie中查找手机号
            var killPhone = $.cookie('killPhone');


            // 验证手机号
            if (! seckill.validatePhone(killPhone)) {

                //绑定phone
                var killPhoneModal = $("#killPhoneModel");

                // 显示弹出层
                killPhoneModal.modal({
                    show : true, // 显示弹出层
                    backdrop: 'static', // 禁止位置关闭
                    keyboard: false // 关闭键盘事件
                });

                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    if (seckill.validatePhone(inputPhone)) {
                        // 手机号写入cookie
                        $.cookie('killPhone',inputPhone,{expire:7,path:'/seckill'});
                        //刷新页面
                        window.location.reload();
                    } else {
                        $('#killPhoneMessage').hide().html('<label label-danger>手机号错误！</label>').show(300);
                    }
                });

            }

            // 已经登录
            // 计时交互
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            $.get(seckill.URL.now(),{}, function (result) {
                if (result && result['success']) {

                    var nowTime = result['data'];
                    //时间判断，计时交互
                    seckill.countdown(seckillId,nowTime,startTime,endTime);
                } else {
                    console.log('result:' + result);
                }

            });

        }
    }
    
    
}