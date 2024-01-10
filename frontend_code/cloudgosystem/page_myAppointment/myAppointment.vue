<template>
	<view class="outside">
		<uni-row class="demo-uni-row">
			<view class="demo-add" v-for="(item, index) in listArr" :key="index">
				<uni-col :span="6">
					<view class="demo-uni-col">
						<image class="avater" :src="userInfoArr[index].photo" mode=""></image>
					</view>
				</uni-col>
				<uni-col :span="12">
					<view class="demo-uni-col" style="padding-left: 20rpx;">
						<view class="friend-name">{{userInfoArr[index].userName}}</view>
						<view class="friend-text">
							{{listArr[index].statusName}}
							{{listArr[index].location}}
						</view>
						<view class="friend-text">
							{{listArr[index].startTime.replace(/^\d{4}-/, "")}} 至
							{{listArr[index].endTime.replace(/^\d{4}-/, "")}}
						</view>

					<!-- 	<view class="friend-text" :class="[item.fontColor == '1' ? 'fontRed' : '',item.fontColor=='2' ? 'fontGray' : '']">
							{{userInfoArr[index].startTime.replace(/^\d{4}-/, "")}} 至
							{{userInfoArr[index].endTime.replace(/^\d{4}-/, "")}}
						</view> -->
					</view>
				</uni-col>
				
				<!-- 右侧 -->
				<uni-col :span="4">
					<view class="demo-uni-col right-bottom" >
						<!-- 	<view class="friend-text" style="margin-bottom: 4rpx;">
								{{item.apply_time.replace(/(\d{4})-/, '')}}
							</view>

							<view v-if="item.response == '1'" class="outside right">
								<image class="dialog" :src="url[0].urlYes" mode=""></image>
								<text class="friend-text" style="margin-top: 2rpx;" >已同意 </text>
							</view>
							
							<uni-section v-if="item.response == '2'" title="对话框示例" type="line" class="outside right">
									<image  @click="dialogToggle(item.rejectReason)" class="dialog" :src="url[1].urlNo" mode=""></image>
									<text  @click="dialogToggle(item.rejectReason)" class="friend-text" style="margin-top: 2rpx;" >不同意 </text>
							</uni-section>
							
							<view v-if="item.response == '0'" class="outside right">
								<image class="dialog" :src="url[2].urlNull" mode=""></image>
								<text class="friend-text" style="margin-top: 2rpx;" >未处理 </text>
							</view> -->
							
							<!-- <view class="outside right">
								<image class="dialog" :src="url[0].support" mode=""></image>
								<text class="friend-text" style="margin-top: 2rpx;" > {{item.like}}</text>
							</view>
							 -->
						</view>
				</uni-col>

			
			</view>
		</uni-row>
		
		<uni-popup ref="alertDialog" type="dialog">
			<uni-popup-dialog 
				:type="msgType" 
				cancelText="关闭" 
				confirmText="好吧" 
				title="拒绝原因" 
				:content="msgType" 
				@confirm="dialogConfirm"
				@close="dialogClose">
			</uni-popup-dialog>
		</uni-popup>
		
		<view class="bottom-text">
			暂时没有更多了
		</view>
	</view>
</template>
	
<script>
	import tidyuptime from '../common/tidyuptime.js'
	import API_PREFIX from "../config.js"
	export default {
		data() {
			return {
				listArr:[],
				friendIdArr:[],
				userInfoArr:[],
				url:[
					// {
					// 	urlYes: '../static/同意.png'
					// },
					// {
					// 	urlNo: '../static/不同意.png'
					// },
					// {
					// 	urlNull: '../static/待处理.png'
					// },
					{
						support: '../static/赞.png'
					}
				],
				type: 'center',
				msgType: 'success',
				messageText: '这是一条成功提示',
				value: ''
			}
		},
		onLoad() {
			this.getmyList();
		},
		methods: {
			getmyList() {
			    uni.request({
					url: API_PREFIX+'/infoservice/friend/'+this.$globalData.userId,
					success: (res) => {
						// let friend = Object.values(res.data.data.friendStatusMap)[0];
						var friendArr = res.data.data.friendStatusList
						for(var i = 0;i<friendArr.length;i++){
							
							//添加用户状态数组
							this.listArr.push(friendArr[i].status)
							//调用获取用户信息函数
							this.getuserInfo(friendArr[i].status)
						}
					}
				});
			}, 
			
			getuserInfo(e) {
				var userId = e.userId
			    uni.request({
					url: API_PREFIX+'/infoservice/stateUser/getUserInfo/'+userId,
					success: (res) => {
						this.userInfoArr.push(res.data.data.user)
					}
				});
			},
			
			// dialogToggle(reason) {
			// 	this.msgType = reason
			// 	// console.log(this.msgType)
			// 	this.$refs.alertDialog.open()
			// },
			// dialogConfirm() {
			// 	console.log('点击确认')
			// 	this.messageText = `点击确认了 ${this.msgType} 窗口`
			// 	// this.$refs.message.open()
			// },
			// dialogClose() {
			// 	console.log('点击关闭')
			// },
		}
	}
</script>



<style lang="scss">
.outside {
	display: flex;
	justify-content: center;
	flex-direction: column;
	align-items: center;
	// margin-top: 10rpx;
}
.demo-uni-row {
	margin-bottom: 10rpx;
	margin-top: 10rpx;
	/* QQ、字节小程序文档写有 :host，但实测不生效 */
	/* 百度小程序没有 :host，需要设置block */
	/* #ifdef MP-TOUTIAO || MP-QQ || MP-BAIDU */
	display: block;
	width:100vw;
	/* #endif */
}
/* 支付宝小程序没有 demo-uni-row 层级 */
/* 微信小程序使用了虚拟化节点，没有 demo-uni-row 层级 */
/* #ifdef MP-ALIPAY || MP-WEIXIN */
 .uni-row {
	width: 100vw;
}
/* #endif */
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
		}
		
		.friend-name {
			font-size: 30rpx;
		}
		
		.friend-text {
			font-size: 22rpx;
		}
		
		.fontRed {
			color: red;
		}
		
		.fontGray {
			color: #ccc;
		}
		
		.dialog {
			width: 50rpx;
			height: 50rpx;
		}	
	}
}

.right-bottom {
	align-items: center;
	.right {
		height: 90rpx;
		// background-color: pink;
	}
}

.bottom-text {
	padding-top: 20rpx;
	font-size: 16rpx;
	color: #808080;
}
</style>
