<template>
	<view>
		<view class="demo-add" v-for="(item, index) in CycleList" :key="index">
		<uni-group mode="card" class="card">
			<uni-title type="h3" title="周期设置" color="#1aad19" align="center"></uni-title>
			
			<view class="uni-px-5">
				<text class="uni-pb-5">我的状态</text>
				<uni-easyinput v-model="CycleList[index].name"  :placeholderStyle="placeholderStyle" placeholder="请输入状态" @blur="changeName(index,$event)"></uni-easyinput>
			</view>
			<view class="uni-px-5">
				<text class="uni-pb-5">我的位置</text>
				<uni-easyinput v-model="CycleList[index].location" :placeholderStyle="placeholderStyle" placeholder="请输入位置" @blur="changelocation(index,$event)"></uni-easyinput>
			</view>
			<view class="uni-px-5" >
				<view style="text-align: left;"> <text class="uni-pb-5" style="position: relative;left: -48%;">开始时间</text> </view>
				<view class="uni-list ">
					<view class="uni-list-cell">
						<view class="uni-list-cell-db" style="position: relative;left: -335%;">
							<picker mode="time" :value="time" v-model="time" start="00:00" end="23:59" @change="changeStartTime(index,$event)" >
								<view class="uni-input">{{formattedStartTime(index)}}</view>
							</picker>
						</view>
					</view>
				</view>
			</view>
			<view class="uni-px-5">
				<view style="text-align: left;"> <text class="uni-pb-5" style="position: relative;left: -48%;">结束时间</text> </view>
				<view class="uni-list">
					<view class="uni-list-cell" style="position: relative;left: -335%;">
						<view class="uni-list-cell-db">
							<picker mode="time" :value="time" start="00:00" end="23:59" @change="changeEndTime(index,$event)">
								<view class="uni-input">{{formattedEndTime(index)}}</view>
							</picker>
						</view>
					</view>
				</view>
			</view>
		<!-- 	v-model="checkboxList[index]" -->
		
		     <uni-data-checkbox multiple v-model="checkboxList[index]" :localdata="range" @change="changeCheckbox(index,$event)"></uni-data-checkbox>
		    
			
			<view class="btn-box">
				<!-- <button class="button popup-success" @click="messageToggle('success') ">
					<text class="button-text success-text">确认修改</text>
				</button> -->
				<button class="default-btn" type="primary" @click="confirmChange(index,$event)">确认修改</button>
				<button class="default-btn" type="primary" @click="deleteCycleconfig(index,$event)">删除设置</button>
				<button class="default-btn" type="primary" @click="addToIndex(index,e)">添加到主页</button>
			</view>
		</uni-group>
		<uni-popup ref="message" type="message">
						<uni-popup-message :type="msgType" :message="messageText" :duration="2000"></uni-popup-message>
		</uni-popup>
		</view>
		<!-- <view class="uni-title uni-common-pl">时间选择器</view>
				<view class="uni-list">
					<view class="uni-list-cell">
						<view class="uni-list-cell-left">
							当前选择
						</view>
						<view class="uni-list-cell-db">
							<picker mode="time" :value="time" start="09:01" end="21:01" @change="bindTimeChange">
								<view class="uni-input">{{time}}</view>
							</picker>
						</view>
					</view>
				</view>
				<view class="uni-title uni-common-pl">时间选择器</view>
						<view class="uni-list">
							<view class="uni-list-cell">
								<view class="uni-list-cell-left">
									当前选择
								</view>
								<view class="uni-list-cell-db">
									<picker mode="time" :value="time" start="09:01" end="21:01" @change="bindTimeChange">
										<view class="uni-input">{{time}}</view>
									</picker>
								</view>
							</view>
						</view> -->
		
	</view>
</template>

<script>
	import { bus } from '../main.js'
	export default{
		data(){
			const currentDate = this.getDate({
			            format: true
			        })
			return { 
				CycleList:[],
				MultichoiceList:[],
				StartTimeList:[],
				EndTimeList:[],
				location:"",
				name:"",
				startTime:'',
				endTime:'',
				week:"",
				values: '',
				password: '',
				
				styles: {
						color: '#2979FF',
						borderColor: '#2979FF'
						},
				checkbox: [0,-1,-1,-1,-1,-1,-1],
				checkboxList:[],
				range: [{"value": 0,"text": "周一"},{"value": 1,"text": "周二"},{"value": 2,"text": "周三"},{"value": 3,"text": "周四"},{"value": 4,"text": "周五"},{"value": 5,"text": "周六"},
				{"value": 6,"text": "周日"}],
				type: 'center',
				msgType: 'success',
				messageText: '修改成功',
				
			}
		},
		computed:{
		        startDate() {
		            return this.getDate('start');
		        },
		        endDate() {
		            return this.getDate('end');
		        }
		    },
			// updated(){
			// 		this.getCycleStatus()
			// },
			mounted(){
				this.getCycleStatus()
				uni.$on('refreshCycle', () => {
				   this.getCycleStatus();
				 })
				
			},
			
			
			// onLoad(){
			// 	this.getCycleStatus()
			// },
		methods: { 
			getCycleStatus() {
				uni.request({
					url: 'http://118.25.40.159:8004/infoservice/cycleConfig/'+this.$globalData.userId,
					success: (res) => {
					console.log(res)
					this.CycleList = res.data.data.privateConfigList
					console.log(this.CycleList)
					this.checkboxList = this.createArray(this.CycleList.length, 7);
					
					this.loadCheckBox()
					}
				});
			},
			changeStartTime(index,e) {
				var that = this
			    that.CycleList[index].startTime = "2000-01-01 "+e.detail.value+":00"
				console.log(index,that.CycleList[index].startTime)
			        },
			changeEndTime(index,e) {
				var that = this
				that.CycleList[index].endTime = "2000-01-01 "+e.detail.value+":00"
				console.log(index,that.CycleList[index].endTime)
					},
			changelocation(index,e){
				var that = this
				that.CycleList[index].location = e.detail.value
				console.log(index,that.CycleList[index].location)
					},
			changeName(index,e){
				var that = this
				that.CycleList[index].name = e.detail.value
				console.log(index,that.CycleList[index].name)
			},
			multiChoice(index,e){
				console.log(e)
				this.MultichoiceList = e.detail.value
				console.log(this.MultichoiceList)
			},
			confirmChange(index,e){
				var cycleList = this.CycleList
				uni.request({
					url:"http://118.25.40.159:8004/infoservice/cycleConfig/updateCycleConfig",
					method:"POST",
					data:{
						  cycleId:cycleList[index].cycleId,
						  userId: this.$globalData.userId,
						  name: cycleList[index].name,
						  location: cycleList[index].location,
						  startTime: cycleList[index].startTime,
						  endTime: cycleList[index].endTime,
						  week: cycleList[index].week
					},
					success:(res)=>{
						console.log(this.CycleList)
						this.getCycleStatus()
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
			deleteCycleconfig(index){
				console.log(index)
				var cycleId = this.CycleList[index].cycleId
				uni.showModal({
				  title: '提示',
				  content: '确认删除？',
				  showCancel: true, // 是否显示取消按钮，默认为 true
				  success:(res)=>{
					  if(res.confirm){
						 uni.request({
						 	url:"http://118.25.40.159:8004/infoservice/cycleConfig/deleteCycleConfig?cycleId="+cycleId,
						 	method:"POST",
						 	success:(res)=>{
						 		console.log(res)
						 		this.getCycleStatus()
						 	},
						 	fail(err){
						 		console.log(err) 
						 	}
						 }) 
					  }
				  }
				});
			},
			addToIndex(index,data){
				uni.request({
					url:"http://118.25.40.159:8004/infoservice/status/updateStatus",
					method:"post",
					data:{"statusId": this.$globalData.stateId,
						  "statusName": this.CycleList[index].name,
						  "location": this.CycleList[index].location,
						  "userId": this.$globalData.userId,
						  "startTime":this.CycleList[index].startTime,
						  "endTime":this.CycleList[index].endTime,
						  "type": 0,
						  "top": true
					},
					success(res){
						uni.$emit('addToIndex');
						console.log(res)
						uni.showModal({
						  title: '提示',
						  content: '添加成功',
						  showCancel: false, // 是否显示取消按钮，默认为 true
						});
					},
					fail(err){
						console.log(err)
					}
				});
				
			},
			formattedStartTime(index){
				const startTimeString = this.CycleList[index].startTime;
				const startTime = new Date(startTimeString);
				const hours = startTime.getHours().toString().padStart(2, "0");
				const minutes = startTime.getMinutes().toString().padStart(2, "0");
				this.StartTimeList[index] = `${hours}:${minutes}`;
				return this.StartTimeList[index]
			},
			formattedEndTime(index){
				const endTimeString = this.CycleList[index].endTime;
				const endTime = new Date(endTimeString);
				const hours = endTime.getHours().toString().padStart(2, "0");
				const minutes = endTime.getMinutes().toString().padStart(2, "0");

				this.EndTimeList[index] = `${hours}:${minutes}`;
				return this.EndTimeList[index]
			},
			getDate(type) {
				const date = new Date();
				let year = date.getFullYear();
				let month = date.getMonth() + 1;
				let day = date.getDate();
				if (type === 'start') {
					year = year - 60;
				} else if (type === 'end') {
					year = year + 2;
				}
				month = month > 9 ? month : '0' + month;
				day = day > 9 ? day : '0' + day;
				return `${year}-${month}-${day}`;
		     },
			 messageToggle(type) {
			 				this.msgType = type
			 				this.messageText = `这是一条${type}消息提示`
			 				this.$refs.message.open()
			},
			loadCheckBox(){
				console.log("执行了加载函数")
				for(var i = 0;i<this.CycleList.length;i++){
					console.log("数组为"+this.checkboxList)
						// this.checkboxList[i].concat([-1,-1,-1,-1,-1,-1,-1])
						// console.log(this.checkboxList[i])
						if(this.CycleList[i].week.includes("周一")){
							this.checkboxList[i][0] = 0;
						}
						if(this.CycleList[i].week.includes("周二")){
							this.checkboxList[i][1] = 1;
						}
						if(this.CycleList[i].week.includes("周三")){
							this.checkboxList[i][2] = 2;
						}
						if(this.CycleList[i].week.includes("周四")){
							this.checkboxList[i][3] = 3;
						}
						if(this.CycleList[i].week.includes("周五")){
							this.checkboxList[i][4] = 4;
						}
						if(this.CycleList[i].week.includes("周六")){
							this.checkboxList[i][5] = 5;
						}
						if(this.CycleList[i].week.includes("周日")){
							this.checkboxList[i][6] = 6;
						}
					// this.checkboxList[i].concat(Array.from(this.checkbox))
					// this.checkbox.fill(-1)
					console.log("数组为"+this.checkboxList[i])
				}
				console.log("数组为"+this.checkboxList)
				
				
			},
			createArray(rows, cols) {
			var arr = new Array(rows);
			for (var i = 0; i < rows; i++) {
			arr[i] = new Array(cols);
			for (var j = 0; j < cols; j++) {
			arr[i][j] = -1;
			}
			}
			return arr;
			},
			changeCheckbox(index,e){
				console.log(e)
				var WeekTostring
				var WeekArry = e.detail.data
				for(var i = 0;i<WeekArry.length;i++){
					if(i==0){
						WeekTostring = WeekArry[i].text
					}else{
						WeekTostring = WeekTostring+'、'+WeekArry[i].text
					}
				}
				console.log(WeekTostring)
				this.CycleList[index].week = WeekTostring
			},
		}
	}
	
</script>

<style>
	.btn-box{
		display: inline-block;
	}
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
		width: 150rpx!important;
		height: 70rpx!important;
		line-height: 70rpx;
		font-size: 27rpx;
		margin: 30rpx;
	}
	.example-body {
			background-color: #fff;
			padding: 10px 0;
		}
	
		.button-text {
			color: #fff;
			font-size: 12px;
		}
		.success-text {
				color: #09bb07;
			}

</style>