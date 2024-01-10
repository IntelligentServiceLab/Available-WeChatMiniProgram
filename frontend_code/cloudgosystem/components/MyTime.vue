<template>
	<view class="page container">
		<view class="example-body">
			<view class="uni-px-5">
			<text class="uni-pb-5">截止时间</text>
			</view>
			<uni-datetime-picker
				style="width:60vw;!important;"
				:clear-icon="false"
				:hide-second = "true"
				type="datetime"
				v-model="datetimesingle" 
				@change="changeLog"
			/>
		</view>
	</view>
</template>

	
<script>
	export default {
		name:"MyTime",
		data() {
			return {
				single: '',
				datetimesingle: '',
				start: Date.now() - 1000000000,
				end: Date.now() + 1000000000,
				formattedTime:""
			}
		},

		watch: {
			datetimesingle(newval) {
				console.log('单选:', this.datetimesingle);
			},
		},
		mounted() {
			() => {
				this.datetimesingle = Date.now() - 2*24*3600*1000
				this.single = '2021-2-12'
			},
			this.getnowTime();
		},
		methods:{
			// 在个位数的前面补0
			paddingZero(num) {
			  return num < 10 ? '0' + num : num;
			},
			// 获取当前时间
			getnowTime() {
				// 获取当前时间
				let now = new Date();
				
				// 获取年、月、日、小时、分钟
				let year = now.getFullYear();
				let month = now.getMonth() + 1; // 月份从0开始，需要加1
				let date = now.getDate();
				let hours = now.getHours();
				let minutes = now.getMinutes();
				let seconds = now.getSeconds();
				// 格式化为字符串
				this.formattedTime = `${year}-${this.paddingZero(month)}-${this.paddingZero(date)} ${this.paddingZero(hours)}:${this.paddingZero(minutes)}:${this.paddingZero(seconds)}`;
			},
			changeLog(e) {
				uni.showLoading({
					title:'修改中'
				})
					let timeRange = [];
					timeRange.push(this.formattedTime);
					timeRange.push(this.datetimesingle)
					this.$emit("getMytime",timeRange);
					uni.hideLoading()
			},
		}
	}
</script>


<style lang="scss">
	.example-body {
		background-color: #fff;
		padding: 10px;
		// width: 80vw;
		display: flex;
		justify-content: space-around;
		align-items: center;
	}
	
	.uni-pb-5 {
		display: inline-block;
		width: 24vw;
	}
</style>
