/**
 * 存放交互js代码
 */
//seckill.detail.init(params);
var seckill = {
		//封装秒杀相关Ajax地址url
    URL: {
        now: function () {
            return '/seckill/time/now';
        },
        exposer: function(seckillId){
            return '/seckill/' + seckillId + '/exposer';
        },
        execution: function(seckillId, md5){
            return '/seckill/' + seckillId + '/' + md5 + '/execution';
        }
    },
//执行秒杀逻辑
    handleSeckill: function(seckillId, node){
        node.hide()
            .html('<button class="btn bg-primary btn-lg" id="killBtn">开始秒杀</button>');
        //post请求
        $.post(seckill.URL.exposer(seckillId),{}, function(result){
            console.log(result.success);
            console.log(result.data);
            
            if(result && result['success']){
                var exposer = result['data'];
                if(exposer['exposed']){
                    //start seckill
                    //get seckill address
                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId, md5);
                    console.log("killUrl:"+killUrl);
                    //只绑定一次点击事件
                    $('#killBtn').one('click', function(){
                        $(this).addClass('disabled');
                        $.post(killUrl, {}, function(result){  
                        	console.log(result);
                            if(result && result['success']){
                                var killResult = result['data'];
                                console.log(killResult);
                                var status = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                console.log(stateInfo);
                                node.html('<span class="label label-success">' + stateInfo + '</span>');
                            }
                        });
                    });
                    node.show();
                }else{
                    //do not start seckill
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    seckill.countdown(seckillId, now, start, end);
                }
            }else{
                console.log('result : ' + result);
            }
        });
    },

    //验证手机号
    validatePhone: function (phone) {
        if (phone && phone.length == 11 && !isNaN(phone)) {
            return true;
        } else {
            return false;
        }
    },

    countdown : function(seckillId, nowTime, startTime, endTime){
        var seckillBox = $('#seckill-box');
        if(nowTime > endTime){
            seckillBox.html('秒杀结束');
        }else if(nowTime < startTime){//秒杀未开始
            var killTime = new Date(Number(startTime) + 1000);
            seckillBox.countdown(killTime, function(event){
                var format = event.strftime('距离秒杀时间： %D天 %H时 %M分 %S秒');
                seckillBox.html(format);
            }).on('finish.countdown', function(){
            	//获取秒杀地址
                seckill.handleSeckill(seckillId, seckillBox);
            });
        }else{
            seckill.handleSeckill(seckillId, seckillBox);
        }
    },

    //详情页秒杀逻辑
    detail: {
        //详情页初始化 
        init: function (params) {
        	 //登陆 验证 计时
            var killPhone = $.cookie('killPhone');
            console.info(killPhone);
            //模拟 验证手机号
            if (!seckill.validatePhone(killPhone)) {
               $('#killPhoneModal').modal({
                    show: true, //显示弹出层
                    backdrop: 'static', //禁止位置关闭
                    keyboard: false  //关闭键盘事件
                });
                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killphoneKey').val();
                    if (seckill.validatePhone(inputPhone)) {
                        $.cookie('killPhone', inputPhone, {expires: 7, path: '/seckill'});
                        //刷新之前写入cookie
                        window.location.reload();
                    } else {
                        $('#killphoneMessage').hide().html('<label class="label label-danger">wrong phone number！</label>').show(300);
                    }
                });
            }

            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];

            $.get(seckill.URL.now(), {}, function (result) {
                if(result && result['success']){
                    var nowTime = result['data'];
                    seckill.countdown(seckillId, nowTime, startTime, endTime);
                }else{
                    console.log('result : ' + result);
                }
            });
        }
    }
};