<template>
	<view >
		<uni-section title="我的状态" type="line">
			<view class="uni-px-5">
				<text class="uni-pb-5">我的状态</text>
				<uni-data-select 
					v-model="value" 
					:localdata="range" 
					@change="change"
					 :clear="false"
					placeholder="选择状态"
					style="width:60vw;!important">
				</uni-data-select>
			</view>
		</uni-section>
		
		<view>
			<!-- 输入框示例 -->
			<uni-popup ref="inputDialog" type="dialog">
				<uni-popup-dialog ref="inputClose" mode="input" title="输入内容" value=""
					placeholder="请输入内容" @close="onCancel" @confirm="dialogInputConfirm"></uni-popup-dialog>
			</uni-popup>
		</view>
	</view>
</template>

<script>
export default {
	name:"MyStatus",
	data() {
		let user = {};
		return {
			value: 1,
			range: [
				{
					value: -1, text: "自定义",
				},
				{ value: 0, text: "办公" },
				{ value: 1, text: "开会" },
				{ value: 2, text: "学习" },
				{ value: 3, text: "运动"},
				{ value: 4, text: "休息" },
			],
			length: 5,
		}
	},
	watch: {
	    value: {
	        handler (newValue, oldValue) {
				if(newValue == -1) {
					this.$refs.inputDialog.open()
				}
	        },
	        immediate: true
	    }
	},
	mounted() {
		this.getUserInfo();
		this.load()
		uni.$on('refreshStatus', () => {
		   this.getPrivateStatus();
		 })
		
	},
    methods: {
		// 拿到个人数据
		getUserInfo() {
			this.user = uni.getStorageSync("personInfo");
			this.value = parseInt(this.user.value)
		},
		// 取消，关闭弹窗
		onCancel() {
		    this.$refs.inputDialog.close() // 隐藏弹窗
		},
		// 确认自定义信息
		dialogInputConfirm(val) {
			this.range.push({ value: this.length, text: val })
			this.value = this.length
			this.change(this.length)
			this.length ++
			// 关闭窗口后，恢复默认内容
			this.$refs.inputDialog.close()
		},
		// 修改id
		change(e) {
			if(e !== -1) {
				uni.showLoading({
					title: '修改中'
				})
			}
			
			setTimeout( () => {
				let mine = {};
				mine.id = this.user.id;
				mine.value = this.value;
				for(let i = 0 ;i < this.range.length;i ++) {
					if(e === this.range[i].value) {
						mine.status = this.range[i].text;
					}
				}
				this.$emit("getMyStatus",mine);
				uni.hideLoading()
			},500)
			},
		getPrivateStatus(){
			console.log(this.$globalData.userId)
			this.range.splice(1)
			uni.request({
				method:"GET",
				url:"http://118.25.40.159:8004/infoservice/privateConfig/"+this.$globalData.userId,
				success: (res) => {
				          // 获取成功后，将获取到的数组添加到range数组中
				          const privateConfigList = res.data.data.privateConfigList;
						
				          const rangeToAdd = privateConfigList.map(item => {
				          	if (item.name !== ""){
				            return {
				              value: item.privateId,
				              text: item.name
				            };
				          								}
				          }).filter(Boolean)
				
				          this.range = [
				            ...this.range,
				            ...rangeToAdd
				          ];
				        },
				fail(err){
					console.log(err)
				}
			})
		},
		load(){
			setTimeout(() => {
			  this.getPrivateStatus();
			}, 2000);
		}
	}
}
</script>

<style>
  .uni-px-5 {
	margin: 10rpx;
	padding-bottom: 10px;
	display: flex;
	/* width: 80vw; */
	justify-content: space-around;
	align-items: center;
	/* background-color: yellow; */
  }

  .uni-pb-5 {
	display: inline-block;
	width: 24vw;
  }
  
  .photo {
	  display: inline-block;
	  width: 60rpx;
	  height: 60rpx;
  }
  
  .dialog-box {
  		padding: 10px;
	}
	
	.dialog-text {
			font-size: 14px;
			color: #333;
		}

</style>