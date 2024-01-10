<template>
	<view class="box">
			<uni-group mode="card" class="card">
			<view style="line"> 
			<button class="default-btn" type="primary" @click="addCycleComponent">添加周期状态</button>
			<button class="default-btn-blue"  @click="addPrivateStatus">添加私人状态</button> 
			<button class="default-btn-blue"  @click="addPrivatePosition">添加私人位置</button> </view>
			</uni-group>
			<uni-fab ref="fab"  :content="unifab.content" :horizontal="unifab.horizontal" :vertical="unifab.vertical"
						:direction="unifab.direction" @trigger="trigger" />
			<!-- <PrivatePattern ref="PrivatePattern"></PrivatePattern> -->
			<PrivateSetting ref="PrivatePattern"></PrivateSetting>
			<CyclePattern ref="CyclePattern"></CyclePattern>
					
				<!-- <CyclePattern ref="CyclePattern" v-for="(componentData, index) in componentList" :key="index" :data="componentData" /> -->
		</view>
			<!-- <view v-if="userInfo.openId === ''" @click="login" class="loginBox">
				<image src="@/static/niming.png"></image>
				<text>点击登录</text>
			</view> -->
			<!-- <view v-else class="loginBox">
				<button open-type="chooseAvatar" @chooseavatar="onChooseAvatar" >
					<image :src="userInfo.headImage"></image>
				</button>

				<input @blur="getnickname" v-model="userInfo.name" type="nickname" class="weui-input" style="text-align: center;"
					placeholder="请输入昵称" />
			</view> -->
			<!-- <uni-title class="title" type="h3" title="周期习惯:" color="#60adfb"></uni-title>
			<uni-table ref="table" :loading="loading">
				<uni-table>
				  <uni-tr>
				    <uni-th width="40" align="left">开始时间：</uni-th>
				  </uni-tr>
				  <uni-tr>
				    <uni-th width="40" align="left">截止时间：</uni-th>
				  </uni-tr>
				  <uni-tr>
				    <uni-th width="40" align="left">状态：</uni-th>
				  </uni-tr>
				  <uni-tr>
				    <uni-th width="40" align="left">地点：</uni-th>
				  </uni-tr>
				  <uni-tr>
				    <uni-th width="40" align="left">操作：</uni-th>
				  </uni-tr>
				</uni-table> -->

			<!-- 	<uni-tr v-for="(item, index) in tableData" :key="index">
					<uni-th align="center">{{ item.start }}</uni-th>
					<uni-td align="center">{{ item.end }}</uni-td>
					<uni-td align="center">{{ item.status }}</uni-td>
					<uni-td align="center">{{ item.location }}</uni-td>
					<uni-td>
						<view class="uni-group">
							<button class="uni-button" type="primary" @click="changeMessage(item)">修改</button>
							<button class="uni-button" type="warn" @click="deleteMessage(item)">删除</button>
						</view>
					</uni-td>
				</uni-tr> -->
				
				<!-- <view v-show="popup" class="popup">
					 <view class="popup-center">
						<view class="pop-texts">
							<input class="pop-text" type="text" placeholder="请输入开始时间" v-model="newStatus.start">
							<input class="pop-text" type="text" placeholder="请输入结束时间" v-model="newStatus.end">
							<input class="pop-text" type="text" placeholder="请输入状态" v-model="newStatus.status" />
							<input class="pop-text" type="text" placeholder="请输入地点" v-model="newStatus.location">
						 </view>
						<view class="pop-btn">
							<button class="btn greenbtn" @click="closeDialog()">取消</button>
							<button v-if="btn.add" class="btn bluebtn" @click="addStatus()">添加</button>
							<button v-if="btn.change" class="btn bluebtn" @click="changeStatus()">修改</button>
						</view>
					</view>
				</view>
			
			</uni-table>
			<button class="default-btn" type="primary" @click="openDialog">添加状态</button> -->
			
		<!-- <button @click="getLoaction">
			获取信息
		</button> -->
<!-- 		<uni-title class="title" type="h3" title="常用状态:" color="#60adfb"></uni-title>
		
		<uni-title class="title" type="h3" title="常用位置:" color="#60adfb"></uni-title> -->
	</view>
</template>

<script>
import tidyuptime from '../../common/tidyuptime.js';
import DataToWeek from '../../common/tidyuptime.js';
import CyclePattern from '../../components/CyclePattern.vue';
import PrivatePattern from '../../components/PrivatePattern.vue'
import PrivateSetting from '../../components/PrivateSetting.vue'
import MyTime from '../../components/MyTime.vue'
export default {
	components: {
	    CyclePattern,
		PrivatePattern,
		PrivateSetting
	  },
	data() {
		return {
			componentList: [], 
			tableData: [],
			CycleList:[],
			popup:false,
			CycleConfig:{
				userId:this.$globalData.userId,
				startTime:"2000-01-01 11:36:00",
				endTime:"2000-01-01 11:36:00",
				name:"吃饭",
				location:"北门123",
				week: "string"
			},
			btn:{
				add:true,
				change:false
			},
			userInfo:{
				openId:'',
				headImage:require('@/static/niming.png'),
				name:'微信用户'
			},
			unifab:{
				title: 'uni-fab',
				directionStr: '垂直',
				horizontal: 'right',
				vertical: 'bottom', 
				direction: 'horizontal',
				pattern: {
					color: '#7A7E83',
					backgroundColor: '#fff',
					selectedColor: '#007AFF',
					buttonColor: '#007AFF',
					iconColor: '#fff'
				},
				popMenu:true,
				is_color_type: false,
				content: [{
						iconPath: '/static/Private.jpg',
						selectedIconPath: '/Private.jpg',
						text: '周期状态',
						active: false
					},
					{
						iconPath: '/static/Cycle.jpg',
						selectedIconPath: '/static/Cycle.jpg',
						
						text: '私人设置',
						active: false
					}
				]
			}
			}
	},
	onHide(){
		uni.$emit('refreshCycle');
		uni.$emit('refreshPrivate');
	 },
	methods: {
		addCycleComponent() {
			const CyclePattern = this.$refs.CyclePattern;
			uni.request({
					url: 'http://118.25.40.159:8004/infoservice/cycleConfig/createCycleConfig',
					method:"POST",
					data:{
					userId:this.$globalData.userId,
					startTime:"2000-01-01 08:00:00",
					endTime:"2000-01-01 09:00:00",
					name:" ",
					location:" ",
					week: "string"
					},
					success: (res) => {
					CyclePattern.getCycleStatus()
					uni.showModal({
						title: '成功添加周期状态',
						showCancel: false,
						confirmText: '确定'
					})
					console.log(res)
					},
					fail(err){
					console.log(err)
					}
				});
		      
		    },
			addPrivateComponent() {
				const PrivatePattern = this.$refs.PrivatePattern;
				uni.request({
						url: 'http://118.25.40.159:8004/infoservice/privateConfig/createPrivateConfig',
						method:"POST",
						data:{
						userId:this.$globalData.userId,
						startTime:"2000-01-01 11:36:00",
						endTime:"2000-01-01 11:36:00",
						name:"",
						location:"",
						},
						success: (res) => {
						PrivatePattern.getPrivateStatus()
						uni.showModal({
							title: '成功添加私人设置',
							showCancel: false,
							confirmText: '确定'
						})
						console.log(res)
						},
						fail(err){
						console.log(err)
						}
					});
			      
			    },
			
		multiChoice(e){
			console.log('e:',e);
			},
			getPrivateStatus() {
				uni.request({
					url: 'http://118.25.40.159:8004/infoservice/privateConfig/'+this.$globalData.userId,
					success: (res) => {
					console.log(res)
					this.PrivateList = res.data.data.privateConfigList
					console.log(this.PrivateList)
					}
				});
			},
		// 获取用户状态
		getCycleStatus() {
			uni.request({
				url: 'http://118.25.40.159:8004/infoservice/cycleConfig/'+this.$globalData.userId,
				success: (res) => {
				console.log(res)
				this.CycleList = res.data.data.privateConfigList
				console.log(this.CycleList)
				}
			});
			
		},
		switch1Change: function (e) {
		            console.log('switch1 发生 change 事件，携带值为', e.detail.value)
		        },
		onShareAppMessage({from,target}){
				    console.log(from,target);
				    let myObj = {
				      title:`欢迎加入云去向小程序！`,
				      path:"/pages/index/index",
				      imageUrl:"/static/icon.png"
				    }
				    return myObj;
				},
		trigger(e){
				console.log(e)
				
				// this.unifab.content[e.index].active = !e.item.active
				console.log(e.index)
				if(e.index==0){
					this.addCycleComponent();
				}
				if(e.index==1){
					this.addPrivateComponent();
				}
				
				},
		addPrivateStatus(){
			const PrivatePattern = this.$refs.PrivatePattern;
			uni.request({
					url: 'http://118.25.40.159:8004/infoservice/privateConfig/createPrivateConfig',
					method:"POST",
					data:{
					userId:this.$globalData.userId,
					startTime:"2000-01-01 11:36:00",
					endTime:"2000-01-01 11:36:00",
					name:" ",
					location:"",
					},
					success: (res) => {
					PrivatePattern.getPrivateStatus()
					uni.showModal({
						title: '成功添加私人状态',
						showCancel: false,
						confirmText: '确定'
					})
					console.log(res)
					},
					fail(err){
					console.log(err)
					}
				});
			
		},
		addPrivatePosition(){
			const PrivatePattern = this.$refs.PrivatePattern;
			uni.request({
					url: 'http://118.25.40.159:8004/infoservice/privateConfig/createPrivateConfig',
					method:"POST",
					data:{
					userId:this.$globalData.userId,
					startTime:"2000-01-01 11:36:00",
					endTime:"2000-01-01 11:36:00",
					name:"",
					location:" ",
					},
					success: (res) => {
					PrivatePattern.getPrivateStatus()
					uni.showModal({
						title: '成功添加私人位置',
						showCancel: false,
						confirmText: '确定'
					})
					console.log(res)
					},
					fail(err){
					console.log(err)
					}
				});
			
		}
		
							// fabClick() {
							// 	uni.showToast({
							// 		title: '点击了悬浮按钮',
							// 		icon: 'none'
							// 	})
							// },
	}
}
</script>

<style lang="scss">
.switch-btn{
	position: relative;
	left: 0%;
	display: inline-block;
	padding: 0rpx;
	margin-top: 20rpx;
	line-height: 70rpx;
	font-size: 28rpx;
}
.title {
	// padding: 4rpx;
	text-indent: 1em;
}	
	
.uni-group {
	display: flex;
	align-items: center;
}

.uni-group {
	display: flex;
	align-items: center;
	justify-content: space-around;
}	
.uni-button {
	display: inline-block;
	box-sizing: border-box;
	padding: 0rpx;
	margin: 0rpx;
	width: 90rpx;
	height: 60rpx;
	text-align: center;
	font-size: 24rpx;
}

.default-btn {
	position: relative;
	left: 0%;
	display: inline-block;
	padding: 0rpx;
	margin-top: 20rpx;
	margin-right: 10rpx;
	width: 180rpx!important;
	height: 70rpx!important;
	line-height: 70rpx;
	font-size: 28rpx;
}
.default-btn-blue{
	position: relative;
	left: 0%;
	display: inline-block;
	padding: 0rpx;
	margin-top: 20rpx;
	margin-right: 10rpx;
	width: 180rpx!important;
	height: 70rpx!important;
	line-height: 70rpx;
	font-size: 28rpx;
	background-color: #00aaff;
	color: white;
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
		text-align: center;
		border-radius: 20rpx;
		padding: 20rpx;
		box-shadow: 0px 0px 10rpx rgba(0, 0, 0, 0.5); /* 灰色阴影 */
		.pop-texts {
			display: flex;
			flex-direction: column;
			justify-content: space-around;
			align-items: center;
			height: 360rpx;
			.pop-text {
				background-color: #ececec;
				width: 300rpx;
				height: 86rpx;
				border-radius: 4rpx;
				font-size: 32rpx;
				color: black;
			}
		}
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
.flex-container{
	display: flex;
	  flex-direction: row;
	  align-items: center;
	  margin: 10rpx;
}
.warp {
		padding: 10px;
	}

	.button {
		margin-bottom: 10px;
	}
</style>