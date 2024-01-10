<template>
	<view>
		<view class="demo-add" v-for="(item, index) in PrivateList" :key="index">
		<uni-group mode="card" class="card">
			<uni-title type="h3" title="私人设置" color="#60adfb" align="center"></uni-title>
			
			<view class="uni-px-5">
				<text class="uni-pb-5">我的状态</text>
				<uni-easyinput v-model="PrivateList[index].name"  :placeholderStyle="placeholderStyle" placeholder="请添加状态"@blur="changeName(index,$event)"></uni-easyinput>
			</view>
			<view class="uni-px-5">
				<text class="uni-pb-5">我的位置</text>
				<uni-easyinput v-model="PrivateList[index].location"  :placeholderStyle="placeholderStyle" placeholder="请添加位置" @blur="changelocation(index,$event)"></uni-easyinput>
				<!-- <uni-easyinput v-model="PrivateList[index].location" :styles="styles" :placeholderStyle="placeholderStyle" :placeholder="PrivateList[index].location"@blur="changelocation(index,$event)"></uni-easyinput> -->
			</view>
			<view class="btn-box">
			<button class="default-btn"  @click="confirmChange(index,$event)">确认修改</button>
			<button class="default-btn"  @click="deletePrivateconfig(index,$event)">删除设置</button>
			<!-- <button class="default-btn" >添加到主页</button> -->
			</view>
		</uni-group>
		</view>
		
	</view>
</template>

<script>
	export default {
		name:"PrivatePattern",
		
		data() {
			return {
			PrivateList:[],	
			valueStatus: false,
			valueLocation: false,
			};
		},
		computed:{
			disableButton(){
				  console.log("结果为"+this.valueLocation+this.valueStatus)
			      return !(this.valueLocation || this.valueStatus)
			    }
		},
		mounted(){
			this.getPrivateStatus()
			uni.$on('refreshPrivate', () => {
			   this.getPrivateStatus();
			 })
		},
		
		methods:{
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
			changelocation(index,e){
				var that = this
				this.valueLocation = true
				that.PrivateList[index].location = e.detail.value
				console.log(index,that.PrivateList[index].location)
					},
			changeName(index,e){
				var that = this
				this.valueStatus = true
				that.PrivateList[index].name = e.detail.value
				console.log(index,that.PrivateList[index].name)
			},
			confirmChange(index,e){
				var privateList = this.PrivateList
				uni.request({
					url:"http://118.25.40.159:8004/infoservice/privateConfig/updatePrivateConfig",
					method:"POST",
					data:{
						  privateId:privateList[index].privateId,
						  userId: this.$globalData.userId,
						  name: privateList[index].name,
						  location: privateList[index].location,		
						  startTime: "2000-01-01 11:36:00",
						  endTime: "2000-01-01 11:36:00"
					},
					success:(res)=>{
						console.log(res)
						this.getPrivateStatus()
						this.changeButton()
						uni.showModal({
						  title: '提示',
						  content: '修改成功',
						  showCancel: false, // 是否显示取消按钮，默认为 true
						});
					},
					fail(err){
						console.log(err)
					}
				})
			},
			deletePrivateconfig(index){
				console.log(index)
				var  privateId = this.PrivateList[index].privateId
				uni.showModal({
					  title: '提示',
					  content: '确认删除？',
					  showCancel: true, // 是否显示取消按钮，默认为 true
					  success:(res)=>{
						  if(res.confirm){
							uni.request({
								url:"http://118.25.40.159:8004/infoservice/privateConfig/deletePrivateConfig?privateId="+privateId,
								method:"POST",
								success:(res)=>{
									console.log(res)
									this.getPrivateStatus()
								},
								fail(err){
									console.log(err) 
								}
							})
						  }
					  }
					});
					
				},
			changeButton() {
						  // 在按钮点击完成后，将 value1、value2 和 value3 重置为 false
						  this.valueStatus = false;
						  this.valueLocation = false;
				 }
	},
	}
</script>

<style>
	.uni-mt-5 {
			margin-top: 5px;
	}
	.uni-pb-5 {
		/* 	background-color: blue; */
		display: inline-block;
		width: 24vw;
		/* font-size: 32rpx; */
		/* color: #666; */
	}
	.uni-px-5 {
		margin: 10rpx;
		padding-bottom: 10px;
		display: flex;
		width: 80vw;
		justify-content: space-around;
		align-items: center;
		/* background-color: yellow; */
	}
	.default-btn {
		position: relative;
		
		display: inline-block;
		padding: 0rpx;
		margin-top: 20rpx;
		width: 220rpx!important;
		height: 70rpx!important;
		line-height: 70rpx;
		font-size: 27rpx;
		margin: 40rpx;
		background-color: #00aaff;
		color: white;
	}
	.btn-box{
		display: inline-block;
	}
</style>