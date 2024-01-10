<template>
	<view>
		<view class="text-area">
			<!-- <text class="title">{{title}}</text> -->
		</view>
		
		<view class="my-subscribe">
			<view class="uni-col">
					<text class="titleFriend">好友列表</text>
			</view>
			<!-- <view class="uni-col">
				<view class="circle" @click="jump_serach()">
					<img class="icon" src="../../static/status/我的预约.png" alt="">
				</view>
				<view class="demo">
					好友搜索
				</view>
			</view> -->
			
		</view>
		
		<view class="outside">
			<uni-row>
				<view class="demo-add" v-for="(item, index) in listArr" :key="index">
					<uni-col :span="6">
						<view class="demo-uni-col" >
							<image class="avater" :src="userInfoArr[index].photo" mode=""></image>
						</view>
					</uni-col>
					<uni-col :span="12">
						<view class="demo-uni-col" style="padding-left: 20rpx;">
						<!-- 好友状态和好友位置 -->
						<view class="friend-name" >
							{{userInfoArr[index].userName}}
							<!-- <image v-if="friendIdArr[index] === 2" class="dialog" src="../../static/好友.jpg" mode="" @click="cancleSubscription(index)"></image> -->
						</view>
							<view class="friend-text">
								{{listArr[index].statusName}}
								{{listArr[index].location}}
								
								<!-- <button class="default-btn" style="height: 15px;width: 20px;margin: 2px;font-size: 3px;" type="primary">置顶</button>
								<button class="default-btn" style="height: 15px;width: 20px;margin: 2px;font-size: 3px;" type="primary" >屏蔽位置</button> -->
							</view>
							<view class="friend-text">
								{{listArr[index].startTime.replace(/^\d{4}-/, "")}} 至
								{{listArr[index].endTime.replace(/^\d{4}-/, "")}}
							</view>
						</view>
					</uni-col>
					
					<!-- 右侧 -->
					<uni-col :span="4">
						<view class="demo-uni-col right-bottom" >
							
								<!-- 好友更新状态的时间 -->
								<!-- <view class="friend-text" style="margin-bottom: 4rpx;">
									{{item.apply_time.replace(/(\d{4})-/, '')}}
								</view> -->
								<image v-if="friendIdArr[index] === 0" class="dialog" src="../../static/2.jpg"  @click="confirmSubscription(index)" mode=""></image>
								<text v-if="friendIdArr[index] === 0" class="friend-text" style="margin-top: 2rpx;" @click="confirmSubscription(index)">订阅好友 	 </text>
								
								<image v-if="friendIdArr[index] === 1" class="dialog" src="../../static/1.jpg"  @click="cancleSubscription(index)" mode=""></image>
								<text v-if="friendIdArr[index] === 1" class="friend-text" style="margin-top: 2rpx;" @click="cancleSubscription(index)">取消订阅 </text>
								
								
								<image v-if="friendIdArr[index] === 2" class="dialog" src="../../static/3.jpg" mode="" @click="cancleSubscription(index)"></image>
								<text v-if="friendIdArr[index] === 2" class="friend-text" style="margin-top: 2rpx;" @click="cancleSubscription(index)">取消订阅</text>
							</view>
					</uni-col>
				</view>
			</uni-row>
			
			<view class="bottom-text">
				暂时没有更多了
			</view>
		</view>
	
		<view v-show="popup" class="popup">
			 <view class="popup-center">
				 <text class="txt">确定要订阅Ta吗？</text>
				<!-- <view class="pop-texts">
					 <input class="pop-text" type="text" placeholder="请输入预约理由" v-model="reason" />
					 <input class="pop-text" type="text" placeholder="请输入预约地点" v-model="location">
					 <input class="pop-text" type="text" placeholder="请输入预约时间" v-model="time">
				 </view> -->
				<view class="pop-btn">
					<button class="btn greenbtn" @click="cancelAdd()">取消</button>
					<button class="btn bluebtn" @click="confirmAdd()">确认</button>
				</view>
			</view>
		</view>
						
		
	</view>
</template>

<script>
	import tidyuptime from '../../common/tidyuptime.js'
	import Alphabetical from '../../common/Alphabetical.js'
	import MyStatus from "../../components/MyStatus.vue"
	import MyStation from "../../components/MyStation.vue"
	import MyTime from "../../components/MyTime.vue"
	import API_PREFIX from "../../config.js"
	export default {
		components:{
			MyStatus,
			MyStation,
			MyTime
		},
		data() {
			return {
				title: '云去向系统',
				subscription:"",
				listArr:[],
				friendlistArr:[],
				friendIdArr:[],
				userInfoArr:[],
				photo:"/staic/icon.png",
				userName:"",
				state:1,
				popup:false,
				reason:"请输入预约理由",
				location:"请输入预约地点",
				time:"请输入预约时间",
				messageText: '这是一条成功提示',
			}
		},
		onLoad() {
			this.getList()
			uni.$on('confirm', () => {
				
				console.log("jieshoudaole")
				someMethod({
				    success(res) {
				      console.log(res);
				      this.shareSubscription(friendId);
				    },
				
				    fail(err) {
				      console.log(err);
				    }
				  });
			  
			 })
		},
		mounted(){
			uni.$on('confirm', () => {
				console.log("jieshoudaole")
				someMethod({
				    success(res) {
				      console.log(res);
				      this.shareSubscription(friendId);
				    },
				
				    fail(err) {
				      console.log(err);
				    }
				  });
			  
			 })
		},
		methods: {
			getuserInfo(e) {
				var userId = e.userId
			    uni.request({
					url: API_PREFIX+'/infoservice/stateUser/getUserInfo/'+userId,
					success: (res) => {
						this.userInfoArr.push(res.data.data.user)
					}
				});
			},
			getList(){
				this.listArr.length = 0
				this.friendlistArr.length = 0
				this.friendIdArr.length = 0
				this.userInfoArr.length = 0
				this.getmyList()
				console.log(this.listArr)
				console.log(this.friendlistArr)
				console.log(this.friendIdArr)
				console.log(this.userInfoArr)
			},
			getmyList() {
			    uni.request({
					//获取到了好友具体信息
					url: API_PREFIX+'/infoservice/friend/'+this.$globalData.userId,
					success: (res) => {
						var friendArr = res.data.data.friendStatusList
						console.log(friendArr)
						for(var i = 0;i<friendArr.length;i++){
							//添加好友状态数组
							this.listArr.push(friendArr[i].status)
							//添加好友信息数组
							this.friendlistArr.push(friendArr[i].friend)
							//添加我关注的人信息
							if(friendArr[i].friend.subscriptionStatus){
								console.log("是互关好友")
								this.friendIdArr.push(2)
							}
							else{
								this.friendIdArr.push(1)
							}
							//调用获取用户信息函数
							this.getuserInfo(friendArr[i].status)
						}
						this.getfansList()
					}
				});
			}, 
			getfansList() {
			    uni.request({
					//获取到了粉丝具体信息
					url: API_PREFIX+'/infoservice/friend/getFansList/'+this.$globalData.userId,
					success: (res) => {
						console.log(res)
						var friendArr = res.data.data.fansStatusLisst
						// this.listArr.length = 0
						// this.friendlistArr.length = 0
						// this.friendIdArr.length = 0
						// this.userInfoArr.length = 0
						console.log("粉丝列表",friendArr)
						for(var i = 0;i<friendArr.length;i++){
							this.listArr.push(friendArr[i].status)
							this.friendlistArr.push(friendArr[i].friend)
							this.friendIdArr.push(0)
							this.getuserInfo(friendArr[i].status)
						}
						console.log(this.friendlistArr)
					}
				});
			},
			// judgeFriend(index,Id){
			// 	console.log("执行了判断")
			// 	if(this.friendIdArr.includes(Id)){
			// 		if(this.friendlistArr[index].subscription){
			// 			return 2
			// 			// this.cancleSubscription()
			// 		}
			// 		else{
			// 			return 1
			// 			// this.cancleSubscription()
			// 		}
			// 	}
			// 	else{
			// 		return 0
			// 		// this.confirmSubsription()
			// 	}
			// },
			cancleSubscription(index){
				var friendUserId = this.userInfoArr[index].userId
				uni.showModal({
				  title: '提示',
				  content: '取消订阅',
				  showCancel: true, // 是否显示取消按钮，默认为 true
				  success:(res)=>{
					  if(res.confirm){
						 uni.request({
						 	url:API_PREFIX+'/infoservice/friend/deleteFriend?userId='+this.$globalData.userId+"&friendUserId="+friendUserId,
						 	method:"POST",
						 	success:(res)=>{
						 		console.log(res)
						 		this.getList()
						 	},
						 	fail(err){
						 		console.log(err)
						 	}
						 })
					  }
				  }
				});
				
			},
			confirmSubscription(index){
				var friendUserId = this.userInfoArr[index].userId
				console.log("订阅了"+this.userInfoArr[index].userId)
				uni.showModal({
				  title: '提示',
				  content: '确认订阅',
				  showCancel: true, // 是否显示取消按钮，默认为 true
				  success:(res)=>{
					  if(res.confirm){
						 uni.request({
						 	url:API_PREFIX+'/infoservice/friend/createFriend',
						 	method:"POST",
						 	data:{
						 		 "userId":this.$globalData.userId ,
						 		  "friendId": friendUserId,
						 		  "subscriptionStatus": true,
						 		  "top": false,
						 		  "shield": false
						 	},
						 	success:(res)=>{
						 		console.log("订阅成功返回"+res)
						 		console.log(res)
						 		this.getList()
						 		
						 	},
						 	fail(err){
						 		console.log(err)
						 	}
						 })
					  }
				  }
				});
				
			},
			shareSubscription(friendId){
				console.log("执行了分享订阅函数")
				uni.request({
					url:API_PREFIX+'/infoservice/friend/createFriend',
					method:"POST",
					data:{
						 "userId":this.$globalData.userId ,
						  "friendId": friendId,
						  "subscriptionStatus": true,
						  "top": false,
						  "shield": false
					},
					success:(res)=>{
						console.log("分享订阅成功返回"+res)
						console.log(res)
						this.getList()
						
					},
					fail(err){
						console.log(err)
					}
				})
			},
			confirmAdd(){
				uni.showLoading();
				setTimeout(()=> {
					this.popup = !this.popup
					uni.showModal({
						title: '订阅成功',
						// content: `预约时间是：${reason},预约地点是：${location},预约时间是：${time}`,
						// content:"你要预约的是："+this.reason,
						showCancel: false,
						confirmText: '确定'
					});
					uni.hideLoading()
				},1000)	
			},
			jump_record() {
				uni.navigateTo({
					url:'/page_friend/record/record'
				})
			},
			jump_serach() {
				uni.navigateTo({
					url:'/page_friend/search/serach'
				})
			},
			onShareAppMessage({from,target}){
			    console.log(from,target);
			    let myObj = {
			      title:`欢迎加入云去向小程序！`,
			      path:"/pages/index/index",
			      imageUrl:"/static/icon.png"
			    }
			    return myObj;
			  }
			
		}
	}
</script>


<style lang="scss">
.text-area {
	display: flex;
	justify-content: center;
	width: 100vw;
	height: 200rpx;
	line-height: 70rpx;
	text-align: center;
	background-color: #0581fd;
	.title {
		font-size: 38rpx;
		font-weight: 500;
		color: white;
	}
}

.my-subscribe {
	position: absolute;
	top:90rpx;
	left: 50%;
	transform: translateX(-50%);
	display: flex;
	align-items: center;
	justify-content: center;
	width: 80vw;
	height: 200rpx;
	border-radius: 20rpx;
	background-color: #e4f2ff;	
	.uni-col {
		width: 50%;
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
				width:40rpx;
				height: 40rpx;
			}
		}
		.demo {
			font-size: 30rpx;
		}
	}
}	
	
.outside {
	margin-top: 100rpx;
	display: flex;
	justify-content: center;
	flex-direction: column;
	align-items: center;
	.demo-add {
		display: flex;
		justify-content: center;
		align-items: center;
		width: 100vw;
		.demo-uni-col {
				display: flex;
				flex-direction: column;
				justify-content: space-around;
				height: 150rpx;
				border-bottom: 1px solid #ccc;
				
				.avater {
					width: 100rpx;
					height: 100rpx;
					margin: 0 auto;
					border-radius: 50%;
					// border: 1rpx solid #0581fd;
				}
				
				.friend-name {
					font-size: 30rpx;
				}
				
				.friend-text {
					font-size: 22rpx;
				}
				
				
				.dialog {
					width: 50rpx;
					height: 50rpx;
				}
				
			}
		}
		.bottom-text {
			margin: 30rpx;
			font-size: 16rpx;
			color: #808080;
		}
	}
.right-bottom {
	align-items: center;
}

.titleFriend{
	  font-family: "Helvetica Neue", Arial, sans-serif;
	  font-size:50rpx;
	  font-weight: bold;
	  color:#009688;
	  letter-spacing: 1px;
	  text-transform: uppercase;
}
// 弹窗
.popup {
	position: fixed;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background-color: rgba(0, 0, 0, 0.5); 
	display: flex;
	justify-content: center;
	align-items: center;
	z-index:999;
	.txt {
		margin-top: 20rpx;
		color: #00aaff;
		font-size: 36rpx;
		font-weight: 600;
		margin-bottom: 30rpx;
	}
	.popup-center {
		background-color: white;
		width: 400rpx;
		// height: 140rpx;
		text-align: center;
		border-radius: 20rpx;
		padding: 20rpx;
		box-shadow: 0px 0px 10rpx rgba(0, 0, 0, 0.5); /* 灰色阴影 */
		// .pop-texts {
		// 	// background-color: pink;
		// 	display: flex;
		// 	flex-direction: column;
		// 	justify-content: space-around;
		// 	align-items: center;
		// 	height: 360rpx;
		// 	.pop-text {
		// 		background-color: #ececec;
		// 		width: 300rpx;
		// 		height: 86rpx;
		// 		border-radius: 4rpx;
		// 		font-size: 32rpx;
		// 		color: black;
		// 	}
		// }
		.pop-btn {
			display: flex;
			justify-content: space-around;
			align-items: center;
			margin-top:40rpx;
			.btn {
				box-sizing: border-box;
				width: 120rpx;
				height:64rpx;
				padding: 0rpx;
				border-radius: 18rpx;
				font-size: 26rpx;
				color: white;
			}
			.greenbtn {
				background-color: #afafaf;
			}
			.bluebtn {
				background-color: #00aaff;
			}
		}
	}
}
</style>
