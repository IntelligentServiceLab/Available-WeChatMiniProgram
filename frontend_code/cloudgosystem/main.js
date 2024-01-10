import App from './App'
// #ifndef VUE3
import Vue from 'vue'
import './uni.promisify.adaptor'
import { createSSRApp } from 'vue'
Vue.config.productionTip = false
App.mpType = 'app'
const app = new Vue({
  ...App
})
app.$mount()
// #endif
// #ifdef VUE3
export function createApp() {
  const app = createSSRApp(App)
  return {
    app
  }
}
// #endif
// import EventBus from 'uni-simple-router/dist/event-bus.js';
Vue.prototype.$globalData = {
  // 你的全局变量
  userId:"",
  stateId:""
}
// Vue.mixin({
//   created() {
//     this.$globalData.userId = this.$options.globalData.userId;
//   }
// });
export const bus = new Vue()
