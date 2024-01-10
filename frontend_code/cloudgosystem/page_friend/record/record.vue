<template>
	<view class="outside">
		<uni-row class="demo-uni-row">
			<view class="demo-add" v-for="item in listArr">
				<uni-col :span="6">
					<view class="demo-uni-col">
						<image class="avater" :src="item.url" mode=""></image>
					</view>
				</uni-col>
				<uni-col :span="12">
					<view class="demo-uni-col" style="padding-left: 20rpx;">
						<view class="friend-name">{{item.name}}</view>
						<view class="friend-text">
							{{item.reason}}
							{{item.location}}
						</view>

						<view class="friend-text" :class="[item.fontColor == '1' ? 'fontRed' : '',item.fontColor=='2' ? 'fontGray' : '']">
							{{item.startTime.replace(/^\d{4}-/, "")}} 至
							{{item.endTime.replace(/^\d{4}-/, "")}}
						</view>
					</view>
				</uni-col>
				
				<!-- 右侧 -->
				<uni-col :span="4">
					<view class="demo-uni-col right-bottom" >
							<view class="friend-text" style="margin-bottom: 4rpx;">
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
							
							<view v-if="item.response == '3'" class="outside right">
								<image class="dialog" :src="url[2].urlNull" mode=""></image>
								<text class="friend-text" style="margin-top: 2rpx;" >未处理 </text>
							</view>
							
							<view v-if="item.response == '0'" class="outside-off right">
								<text class="bottom-txt" @click="dialogSucess()">拒绝</text>
								<text class="bottom-txt" @click="dialogFail()">通过</text>
							</view>
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
		
		<uni-popup ref="alertSucess" type="dialog">
			<uni-popup-dialog 
				:type="msgType" 
				cancelText="关闭" 
				confirmText="好的" 
				title="已经拒绝" 
			
				@confirm="dialogConfirm"
				@close="dialogClose">
			</uni-popup-dialog>
		</uni-popup>
		
		<uni-popup ref="alertFail" type="dialog">
			<uni-popup-dialog 
				:type="msgType" 
				cancelText="关闭" 
				confirmText="好的" 
				title="已经通过" 
			
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
	import tidyuptime from '../../common/tidyuptime.js'
	export default {
		data() {
			return {
				listArr:[],
				url:[
					{
						urlYes: '../../static/同意.png'
					},
					{
						urlNo: '../../static/不同意.png'
					},
					{
						urlNull: '../../static/待处理.png'
					}
				],
				type: 'center',
				msgType: 'success',
				messageText: '这是一条成功提示',
				value: ''
			}
		},
		onLoad() {
			this.getList();
		},
		methods: {
			getList() {
			    uni.request({
					url: 'http://127.0.0.1:4523/m1/3371748-0-default/record',
					success: (res) => {
						this.listArr = res.data
						this.listArr = tidyuptime.sortSatrtTime(this.listArr);
						this.listArr = tidyuptime.deleteTime(this.listArr);
						this.listArr = tidyuptime.SignTime(this.listArr);
					}
				});
			},
			dialogToggle(reason) {
				this.msgType = reason
				this.$refs.alertDialog.open()
			},
			dialogSucess(reason) {
				this.$refs.alertSucess.open()
			},
			dialogFail(reason) {
				this.$refs.alertFail.open()
			},
			dialogConfirm() {
				console.log('点击确认')
				this.messageText = `点击确认了 ${this.msgType} 窗口`
				// this.$refs.message.open()
			},
			dialogClose() {
				console.log('点击关闭')
			},
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
		
		.friend-text {
			font-size: 22rpx;
		}
		
		.friend-name {
			font-size: 30rpx;
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

// 弹窗
// .popup {
// 	position: fixed;
// 	top: 0;
// 	left: 0;
// 	width: 100%;
// 	height: 100%;
// 	background-color: rgba(0, 0, 0, 0.5); 
// 	display: flex;
// 	justify-content: center;
// 	align-items: center;
// 	z-index:999;
// 	.popup-center {
// 		background-color: white;
// 		width: 300rpx;
// 		height: 140rpx;
// 		text-align: center;
// 		border-radius: 20rpx;
// 		padding: 20rpx;
// 		box-shadow: 0px 0px 10rpx rgba(0, 0, 0, 0.5); /* 灰色阴影 */
// 	}
// }

		
.outside-off {
	display: flex;
	justify-content: space-around;
	width: 160rpx;
	padding: 0rpx;
	border-radius: 6rpx;
	font-size: 24rpx;
	.bottom-txt {
		border: 1rpx solid #ccc;
		border-radius: 6rpx;
		background-color: #00aaff;
		height: 40rpx;
		width: 60rpx;
		text-align: center;
		line-height: 40rpx;
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
