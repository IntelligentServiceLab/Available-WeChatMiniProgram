<template>
	<view class="content">
		
		<view class="text-area">
			<image class="icon" style="height: 90rpx;width:100rpx"  src="/static/LOGO.jpg"></image>
			<text class="title">{{title}}</text>
		</view>
		<!-- <view class="loginBox">
			<button open-type="chooseAvatar" @chooseavatar="onChooseAvatar" >
				<image :src="userInfo.headImage"></image>
			</button>
		
			<input @blur="getnickname" v-model="userInfo.name" type="nickname" class="weui-input" style="text-align: center;"
				placeholder="请输入昵称" />
		</view> -->
		<uni-row>
			<uni-col :span="12" >
				<view class="circle">
					
					<button class="circle"  open-type="chooseAvatar" @chooseavatar="onChooseAvatar" >
						<image class="icon" style="height: 65rpx;width:65rpx"  :src="userInfo.photo"></image>
					</button>
				</view>
				<view class="demo-uni-col">
					<input @blur="getnickname" v-model="userInfo.userName" type="nickname" class="weui-input" style="text-align: center;"
						placeholder="新用户" />
				</view>
			</uni-col> 
			<uni-col :span="12" >
				<view class="circle" @click="jump_myAppointment">
					<img class="icon" src="../../static/status/我的预约.png" alt="">
				</view>
				<view class="demo-uni-col">
					我的订阅
				</view>
			</uni-col> 
			<!-- <uni-col :span="12">
				<view class="circle" @click="jump_bookMine()">
					<img class="icon" src="../../static/status/注册.png" alt="">
				</view>
				<view class="demo-uni-col">
					好友订阅
				</view>
			</uni-col> -->
		</uni-row>
		<view class="example-body">
<!-- 			<button class="default-btn" type="primary" @click="changeStatus()">切换模式</button> -->
		</view>
		<!-- 通用模式 -->
		<view class="box" id="public">
			<uni-section class="unisection" title="当前状态" type="line">
				<uni-group mode="card" class="card" style="text-align: center;">
					<uni-title type="h3" title="当前状态" color="#60adfb" align="center"></uni-title>
					<view class="status"> 
						<img class="photo" :src=mineStatus.url alt="">
						<text>{{mineStatus.text}}</text>
						<text>{{mineStatus.location}}</text>
					</view>
					<text style="font-size: 26rpx; ">{{mineStatus.startTime}} ~ {{mineStatus.endTime}}</text>
				</uni-group>
			</uni-section>

			<MyStatus @getMyStatus = "getMyStatus"></MyStatus>
			<MyStation @getStation="getStation" @getGPS="getGPS"></MyStation>  
			<MyTime @getMytime="getMytime"></MyTime>
			<button class="default-btn" type="primary" :disabled="disableButton"  @click="changeStatus">确认修改</button>
		
		</view>
			
		</view>
		
	</view>
</template>

<script>
	import MyStatus from "../../components/MyStatus.vue"
	import MyStation from "../../components/MyStation.vue"
	import MyTime from "../../components/MyTime.vue"
	import API_PREFIX from "../../config.js"
	import { bus } from '../../main.js'
	export default {
		components:{
			MyStatus,
			MyStation,
			MyTime,
		},
		data() {	
			return {
				title: '云去向系统',
				secret1:"6ea24e95a938e50d",
				secret2:"373f3c1e7d665041",
				mineStatus:{
					id:0,
					userId:"",
					statusId:"",
					text:"",
					location:"",
					url:'',
					showImg:true,
					statusName:"",
					startTime:"",	
					endTime:"",
					type:0,
					},
				userInfo:{
						userId:"",
						openId:'',
						photo:require('@/static/niming.png'),
						userName:'微信用户',
						createTime:"",					
						updateTime:"",
					},
				list:[
					{id:0,text:"办公中",url_default:'../../static/status/办公gray.png',url:'../../static/status/办公.png',showImg:true},
					{id:1,text:"开会中",url_default:'../../static/status/共享办公桌gray.png',url:'../../static/status/共享办公桌.png',showImg:true},
					{id:2,text:"学习中",url_default:'../../static/status/学习gray.png',url:'../../static/status/学习.png',showImg:true},
					{id:3,text:"运动中",url_default:'../../static/status/跑步gray.png',url:'../../static/status/跑步.png',showImg:true},
					{id:4,text:"休息中",url_default:'../../static/status/休息一下gray.png',url:'../../static/status/休息一下.png',showImg:true},
					{id:5,url:"../../static/status/未知格式.png"}
				],
				valueStatus: false,
				valueLocation: false,
				valueTime: false
				
			}
		},
		computed:{
			disableButton(){
				  console.log("结果为"+this.valueLocation+this.valueStatus+this.valueTime)
			      return !(this.valueLocation || this.valueStatus || this.valueTime)
			    }
		},
		mounted(){
			uni.$on('addToIndex', () => {
			   this.getMine(this.userInfo.userId);
			 })
			 
			 this.intervalId = setInterval(() => {
			     this.checkTimeAndExecute();
			   }, 60*1000);
		},
		beforeDestroy() {
		  // 清除定时器
		  clearInterval(this.intervalId);
		},
		onShow() {
		    // 页面显示时执行的函数
			if(this.$globalData.userId){
				uni.$emit('refreshLocation');
				uni.$emit('refreshStatus');
				console.log("执行了重新加载")
			}
			this.checkTimeAndExecute();
			
		  },
		methods: {	
			created() {
			    // 监听事件
			    
			  },
			onLoad(query) {
				this.login()
				console.log(query)
				if (query.friendId) {
				        // 执行特定的方法
				       uni.$emit('confirm')
					   console.log("接受到了用户Id",query.friendId)
				    }
			},
			login(){
				//用户登录
					console.log('logining!!!')
					if (this.userInfo.openId === "") {
						var that = this
						wx.login({
							success(res) {
								// console.log('用户code ===', res.code)
								that.getUserOpenId(res.code)
								console.log(res.code)
								console.log('登陆成功')
							},
							fail(err) {
								uni.$showMsg('登陆失败')
							}
						})
					}
			},
			
			//获取用户openId并传回数据库查询返回
			getUserOpenId(code) {
				var that = this
				wx.request({
					url: 'https://api.weixin.qq.com/sns/jscode2session',
					method: 'GET',
					timeout: 100000,
					data: {
						appid: 'wxef0c32edda211a22',
						js_code: code,
						secret: that.secret1+that.secret2,
						grand_type: 'authorization_code',
						// openId:'res.data.openid'
					},
					success:(res) =>{
						console.log(res)
						this.userInfo.openId = res.data.openid
						console.log('用户openid ===', this.userInfo.openId)
						//保证请求后端接口的操作在获取openId之后
						uni.request({
							url:API_PREFIX+"/infoservice/stateUser/login?openId=" + this.userInfo.openId,
							method:"POST",
							data:{
								  openId: this.userInfo.openId,
							},
							success:(res)=>{
								console.log(res)
								//调用设置用户信息函数
								
								this.setUserInfo(res)
								console.log(res)
								if(res.data.message==="注册成功！"){
									this.createStatus()
								}
								//加载用户信息
								this.getMine(this.userInfo.userId)
							},
							fail(err){
								console.log("登录接口失败")
								console.log(err)
							}
						});
					},
					fail(err) {
						console.log('失败', err)
					}
				});
				
			},
			//载入个人信息
			setUserInfo(e){
				this.$globalData.userId = e.data.data.user.userId
				this.userInfo.userId = e.data.data.user.userId
				this.userInfo.userName = e.data.data.user.userName
				this.userInfo.photo = e.data.data.user.photo
				this.userInfo.createTime = e.data.data.user.createTime
				this.userInfo.updateTime  = e.data.data.user.updateTime
				this.userInfo.openId = e.data.data.user.openId
			},
			// 拿到个人状态信息
			getMine(e) {
				const url = API_PREFIX+'/infoservice/status/'+e
				const prefix = "2000-01-01";
				
			    uni.request({
					url,
					// url: 'http://127.0.0.1:4523/m1/3371748-0-default/status',
					method:"GET",
					data:{
							
					},
					success: (res) => {
						console.log("重新渲染")
						console.log(res)
							this.$globalData.stateId = res.data.data.status.statusId
							this.mineStatus.statusId = res.data.data.status.statusId
							this.mineStatus.location = res.data.data.status.location
							this.mineStatus.text = res.data.data.status.statusName
							if (res.data.data.status.startTime.startsWith(prefix)) {
							        this.mineStatus.startTime = res.data.data.status.startTime.substring(prefix.length); // 去掉开头部分内容
							}else{
								this.mineStatus.startTime = res.data.data.status.startTime
							}
							if (res.data.data.status.endTime.startsWith(prefix)) {
							        this.mineStatus.endTime = res.data.data.status.endTime.substring(prefix.length); // 去掉开头部分内容
							}else{
								this.mineStatus.endTime = res.data.data.status.endTime
							}
							this.mineStatus.url = '../../static/status/休息一下.png'
							uni.setStorageSync("personInfo", res.data)
							for(let x=0;x<this.list.length;x++) {
								if(this.list[x].text == res.data.statusName) {
									this.list[x].showImg = false
								}
							}
						
					},
					fail:(err) => {
						console.log("访问失败")
						console.log(err)
					}
				});
			},
			createStatus() {
				uni.request({
					url:API_PREFIX+"/infoservice/status/createStatus",
					method:"post",
					data:{
						  "statusName": "办公",
						  "location": "图书馆",
						  "userId": this.userInfo.userId,
						  "startTime":"2023-01-01 7:00:00",
						  "endTime":"2023-01-01 8:00:00",
						  "type": 0,
						  "top": true
					},
					success:(res)=>{
						console.log(res)
						this.getMine(this.userInfo.userId)
					},
					fail(err){
						console.log(err)
					}
				});
			},
			changeStatus() {
				var that = this
				uni.request({
					url:API_PREFIX+"/infoservice/status/updateStatus",
					method:"post",
					data:{"statusId": this.mineStatus.statusId,
						  "statusName": this.mineStatus.text,
						  "location": this.mineStatus.location,
						  "userId": this.userInfo.userId,
						  "startTime":this.mineStatus.startTime,
						  "endTime":this.mineStatus.endTime,
						  "type": 0,
						  "top": true
					},
					success:(res)=>{
						console.log(res)
						this.changeButton();
						// this.getMine(this.userInfo.userId)
						uni.showModal({
						  title: '提示',
						  content: '修改成功',
						  showCancel: false, // 是否显示取消按钮，默认为 true
						});
					},
					fail(err){
						console.log(err)
					}
				});
			},
			changeUserInfo(){
				uni.request({
					url:API_PREFIX+"/infoservice/stateUser/updateUser",
					method:"POST",
					data:{
						  "userId": this.userInfo.userId,
						  "userName":this.userInfo.userName,
						  "photo": this.userInfo.photo,
						  "createTime":this.userInfo.createTime,
						  "updateTime": this.userInfo.updateTime,
						  "openId":this.userInfo.openId 
					},
					success:(res)=>{
						console.log(res)
						uni.showToast({
						  title: '修改成功',
						  icon: 'none',
						  duration: 2000
						});
						// console.log(this.userInfo.userId,this.userInfo.photo,this.userInfo.createTime)
					}
				})
				
			},
			getMyStatus(e) {
				
				this.mineStatus.id = e.id ;
				this.mineStatus.text = e.status;
				this.valueStatus = true
				let determine = 0;
				for(let i = 0;i < this.list.length;i ++) {
					if(e.value === this.list[i].id) {
						this.mineStatus.url = this.list[i].url;
						determine = 1;
					}
				};
				if(determine === 0) {
					this.mineStatus.url = this.list[5].url;
				}
				// this.changeStatus()
				console.log(this.mineStatus)
			},
			getStation(e) {
				this.valueLocation = true
				console.log('输出为'+e.location)
				this.mineStatus.location = e.location
				// this.changeStatus()
			},
			getMytime(e) {
				this.valueTime = true
				this.mineStatus.startTime = e[0]
				this.mineStatus.endTime = e[1]+":00"
				// this.changeStatus()
			},
			onChooseAvatar(e) {
				this.userInfo.photo = e.detail.avatarUrl
				this.changeUserInfo()
				console.log(e.detail)
			},
			//获取昵称
			getnickname(e) {
				console.log(e.detail)
				this.userInfo.userName = e.detail.value;
				this.changeUserInfo()
			},
			getGPS(e) {
			  const that = this;
			  wx.getLocation({
			    type: 'wgs84',
			    success: (res) => {
			      var latitude = res.latitude; // 纬度
			      var longitude = res.longitude; // 经度
			      var speed = res.speed;
			      var accuracy = res.accuracy;
			      that.getCity(latitude, longitude, e);
			      console.log('执行了该函数'+longitude+','+latitude);
			    },
				fail(err){
					console.log(err)
				}
			  });
			},
			getCity: function(lat, long, e) { 
			        var that = this
			        var url = "https://api.map.baidu.com/reverse_geocoding/v3/";
			        var latitude = lat; // 纬度
			        var longitude = long; // 经度
			        var params = {
			          ak: "0b7IelTlBjK01LbKQOB0MOSQwl21YVzV", // 百度地图密钥
			          output: "json",
			          location: latitude + "," + longitude
			        }
			        wx.request({
			          url: url,
			          data: params,
			          success:(res) =>{
			            // 获取到地址数据结束刷新
			            wx.hideLoading();
						console.log(res)
			            // 获取地址
			            console.log(res.data.result.formatted_address)
						e.location = res.data.result.formatted_address
						that.getStation(e)
						
			          }
			        })
			      },
			jump_myAppointment() {
				  	uni.navigateTo({
				  		url:'/page_myAppointment/myAppointment'
				  	})
				  },
			jump_bookMine() {
				  	uni.navigateTo({
				  		url:'/page_bookMine/bookMine'
				  	})
				  },
			onShareAppMessage({from,target}){
					console.log("执行了分享函数")
				      let myObj = {
				        title:`欢迎加入云去向小程序！`,
				        path:"/pages/index/index?friendId="+this.$globalData.userId,
				        imageUrl:"/static/icon.png",
				      }
					  console.log(myObj);
				      return myObj;
				    },
			changeButton() {
					      // 在按钮点击完成后，将 value1、value2 和 value3 重置为 false
					      this.valueStatus = false;
					      this.valueLocation = false;
					      this.valueTime = false;
				 },
			checkTimeAndExecute() {
				console.log("执行函数")
				   const now = new Date();
				   const currentTime = now.toTimeString().slice(0, 8); // 获取当前时间的小时、分钟和秒，格式为 HH:mm:ss
					const targetTime1 = this.mineStatus.endTime
				   // 检查第一种格式：YYYY-MM-DD HH:mm:ss
				   if (/\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}/.test(targetTime1)) {
				     const [targetDate, targetTime] = targetTime1.split(" ");
				     const [targetHour, targetMinute, targetSecond] = targetTime.split(":");
				     const currentDate = now.toISOString().slice(0, 10); // 获取当前日期，格式为 YYYY-MM-DD
				 
				     if (
				       currentDate === targetDate &&
				       now.getHours() === parseInt(targetHour, 10) &&
				       now.getMinutes() === parseInt(targetMinute, 10) &&
				       now.getSeconds() >= parseInt(targetSecond, 10)
				     ) {
				       // 当系统时间大于或等于设定时间时执行的函数
				       // 在这里替换为你想要执行的函数
				       this.changeSilenceStatus();
				       this.load();
				     }
				   }
				 
				   // 检查第二种格式：HH:mm:ss
				   if (/\d{2}:\d{2}:\d{2}/.test(targetTime1)) {
				     const [targetHour, targetMinute, targetSecond] = targetTime1.split(":");
				 
				     if (
				       now.getHours() === parseInt(targetHour, 10) &&
				       now.getMinutes() === parseInt(targetMinute, 10) &&
				       now.getSeconds() >= parseInt(targetSecond, 10)
				     ) {
				       // 当系统时间大于或等于设定时间时执行的函数
				       // 在这里替换为你想要执行的函数
				       this.changeSilenceStatus();
				       this.load();
				     }
				   }
				 },
				 changeSilenceStatus() {
				 	var that = this
				 	uni.request({
				 		url:API_PREFIX+"/infoservice/status/updateStatus",
				 		method:"post",
				 		data:{"statusId": this.mineStatus.statusId,
				 			  "statusName": "未知",
				 			  "location": "未知",
				 			  "userId": this.userInfo.userId,
				 			  "startTime":null,
				 			  "endTime":null,
				 			  "type": 0,
				 			  "top": true
				 		},
				 		success:(res)=>{
				 			console.log(res)
				 		},
				 		fail(err){
				 			console.log(err)
				 		}
				 	});
				 },
				 load(){
				 	setTimeout(() => {
				 	  this.getMine(this.$globalData.userId);
				 	}, 2000);
				 }
				  // getPrivateStatus(e) {
				  // 	this.PrivateStatus.id = e.id ;
				  // 	this.PrivateStatus.text = e.status;
				  // 	let determine = 0;
				  // 	for(let i = 0;i < this.list.length;i ++) {
				  // 		if(e.value === this.list[i].id) {
				  // 			this.PrivateStatus.url = this.list[i].url;
				  // 			determine = 1;
				  // 		}
				  // 	};
				  // 	if(determine === 0) {
				  // 		this.PrivateStatus.url = this.list[5].url
				  // 	}
				  // },
				  // getPrivateStation(e) {
				  // 	this.PrivateStatus.location = e.location
				  // },
				  // getPrivatetime(e) {
				  // 	this.PrivateStatus.time = e
				  // },
				  //使用插件的方式调用高德地图接口
			  //  getCity(e) {
			  //    var that = this;
				 // var amapFile = require('../../libs/amap-wx.130.js')
			  //    var myAmapFun = new amapFile.AMapWX({key:'153c1c41c78c0000d77c739583592a24'});
			  //    myAmapFun.getRegeo({
			  //      success(res){
					//    console.log(res[0].name)
					//    var location = res[0].name
					//    that.mineStatus.location = location
			  //        //成功回调
			  //      },
			  //      fail(info){
			  //        //失败回调
			  //        console.log(info)
			  //      }
			  //    })
			  //  },
			

		}
	}
</script>

<style scoped lang="less">
	.content {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}

	.text-area {
		display: flex;
		justify-content: center;
		width: 100vw;
		height: 200rpx;
		line-height: 70rpx;
		text-align: center;
		background-color: #0581fd;
	}

	.title {
		margin-top: 10rpx;;
		font-size: 38rpx;
		font-weight: 500;
		color: white;
	}
	

	::v-deep .uni-row {
		position: absolute;
		top:90rpx;
		margin: 0 auto;
		width: 80vw;
		height: 200rpx;
		display: flex;
		align-items: center;
		border-radius: 20rpx;
		background-color: #e4f2ff;	
		.uni-col {
			height: 160rpx;
			border-radius: 30rpx;
			display: flex;
			flex-direction: column;
			justify-content: space-around;
			align-items: center;
			.circle {
				display: flex;
				align-items: center;
				justify-content: center;
				width: 110rpx;
				height: 110rpx;
				background-color: #fff;
				border-radius: 50%;
				.icon {
					display: inline-block;
					width:80rpx;
					height:80rpx;
					border-radius: 50%;
				}
			}
			.demo-uni-col {
				font-size: 30rpx;
			}
		}
	}	
	
	.example-body {
		margin-top: 150rpx;
		width: 86vw;
		// background-color:pink;
	}
	
	.box {
		display: block;
		flex-direction: column;
		justify-content: space-around;
		align-items: center;
		width: 90vw;
		// height: 600rpx;
		margin-bottom: 20rpx;
		box-shadow: 0 1rpx 12rpx #e4f2ff, 0 0 16rpx #e4f2ff;
		text-decoration: none;
		border-radius: 10rpx;
	}
	
	/deep/ .unisection .card {
		margin: 0rpx;
		background-color: pink;
		width: 100vw;
		height: 20rpx;
		padding: 0rpx;
	}

	.card /deep/ .status {
		display: flex;
		align-items: center;
		justify-content: space-around;
		// background-color: pink;
		width: 74vw;
		height:60rpx;
		.photo {
			display: inline-block;
			width: 60rpx;
			height: 60rpx;
		}
		text {
			font-size: 24rpx;
		}
	}
	.default-btn {
		position: relative;
		left: 31%;
		display: inline-block;
		padding: 0rpx;
		margin-top: 30rpx;
		margin-bottom: 30rpx;
		width: 450rpx!important;
		height: 60rpx!important;
		line-height: 50rpx;
		font-size: 28rpx;
	}
	.loginBox{
		display: flex;
		align-items: center;
		justify-content: center;
		flex-direction: column;
		image{
			width: 250rpx;
			height: 250rpx;
		}
	}
	
	
</style>
