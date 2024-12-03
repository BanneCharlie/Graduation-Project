import Vue from 'vue'

import Cookies from 'js-cookie'

import Element from 'element-ui'
import './assets/styles/element-variables.scss'

import 'jquery'

import '@/assets/styles/index.scss' // global css
import '@/assets/styles/ruoyi.scss' // ruoyi css
import App from './App'
import store from './store'
import router from './router'
import permission from './directive/permission'

import './assets/icons' // icon
import './permission' // permission control
import { getDicts } from "@/api/system/dict/data";
import { getUnitTypeDict , getDeviceCateDict , getPaymentTypeDict} from "@/api/business/dict/data";
import { getConfigKey } from "@/api/system/config";
import { parseTime, resetForm, addDateRange, selectDictLabel, selectDictLabels, download, handleTree,downloadFile ,showImage , checkArryIsExistElement} from "@/utils/ruoyi";
import Pagination from "@/components/Pagination";
// 自定义表格工具扩展
import RightToolbar from "@/components/RightToolbar"
import VueUeditorWrap from 'vue-ueditor-wrap'

// 全局方法挂载
Vue.prototype.getDicts = getDicts
Vue.prototype.getUnitTypeDict = getUnitTypeDict
Vue.prototype.getDeviceCateDict = getDeviceCateDict
Vue.prototype.getPaymentTypeDict = getPaymentTypeDict

Vue.prototype.getConfigKey = getConfigKey
Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.selectDictLabel = selectDictLabel
Vue.prototype.selectDictLabels = selectDictLabels
Vue.prototype.download = download
// 我的全局文件下载方法
Vue.prototype.downloadFile = downloadFile
Vue.prototype.showImage = showImage
Vue.prototype.handleTree = handleTree
Vue.prototype.checkArryIsExistElement = checkArryIsExistElement

Vue.prototype.msgSuccess = function (msg) {
  this.$message({ showClose: true, message: msg, type: "success" });
}

Vue.prototype.msgError = function (msg) {
  this.$message({ showClose: true, message: msg, type: "error" });
}

Vue.prototype.msgInfo = function (msg) {
  this.$message.info(msg);
}

// 全局组件挂载
Vue.component('Pagination', Pagination)
Vue.component('RightToolbar', RightToolbar)

Vue.component('vue-ueditor-wrap', VueUeditorWrap)


Vue.use(permission)

/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online! ! !
 */

Vue.use(Element, {
  size: Cookies.get('size') || 'medium' // set element-ui default size
})

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
