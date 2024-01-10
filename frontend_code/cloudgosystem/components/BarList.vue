<template>
<!-- 	<view class="outside"> -->
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
							{{item.status}}
							{{item.location}}
						</view>
						<view class="friend-text">
							{{item.startTime.replace(/^\d{4}-/, "")}} 至
							{{item.endTime.replace(/^\d{4}-/, "")}}
						</view>
					</view>
				</uni-col>
				<uni-col :span="4">
					<view class="demo-uni-col" >
						<view class="friend-text">
							{{item.apply_time.replace(/(\d{4})-/, '')}}
						</view>
						<image class="dialog" src="../static/friend/预约.png" @click="Booking()" mode=""></image>
						<modal v-if="areaShow" title="新增空间" confirm-text="保存" cancel-text="取消" @cancel="cancelAdd" @confirm="confirmAdd">
						<input type="text" v-model="areaTxt" placeholder="请输入预约时间和地点" class="intxt" minlength="5" />
						</modal>
					</view>
				</uni-col>
			</view>
			nihao
		</uni-row>
	<!-- </view> -->
</template>

<script>
	import tidyuptime from '../common/tidyuptime.js'
	export default {
		name:"BarList",
		props:{
			listArr: String
		},
		data() {
			return {
				listArr:[],
				areaShow:false,
				areaTxt:'',
			}
		},
		mounted() {
			
		},
		onLoad() {
			// this.getList();
			this.sendMsg();
		},
		methods: {
			sendMsg() {
				console.log("hahah")
			},
			Booking(){
				this.areaShow = true
			},
			cancelAdd(){
				this.areaShow = false
				uni.showModal({
					title: '预约失败',
					showCancel: false,
					confirmText: '确定'
				});
			},
			confirmAdd(){
				this.areaShow = false
				uni.showModal({
					title: '已预约',
					content: '预约时间和地点为：'+this.areaTxt,
					showCancel: false,
					confirmText: '确定'
				});
			},
		}
	}
</script>



<style lang="scss">
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
		// width: 90vw;
		height: 140rpx;
		border-bottom: 1px solid #ccc;
		// background-color: pink !important;
		
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
	padding-top: 20rpx;
	font-size: 16rpx;
	color: #808080;
	// margin: 0 auto;
}
</style>
