<template>
 <div>
   <el-container>
      <el-main>
         <el-row>
           <el-col :xs="24" :lg="12" :xl="12" style="text-align: center;">
            <div class="content-view" v-html="rawHtml">
            </div>
            </el-col>
         </el-row>
      </el-main>
    </el-container>
 </div>
</template>

<script>
import {showTemplateReportData} from '@/api/qrcode'
import ruoyi from '@/utils/ruoyi'

export default {
 data() {
  return {
     // 二维码中包含的templateId
      reportId:'',
      rawHtml: '',
      qrcodeImageServerPath:'',
      showImageServerInterfacePath:'',
  };
 },
 created() {
   this.reportId = this.$route.query.reportId;
   this.showImageServerInterfacePath = ruoyi.baseURL + ruoyi.showImagePath;
 },
 mounted() {
   this.phoneScanThisVue()
 },

 methods: {
    // 获取当前模板 html 信息
    phoneScanThisVue() {
      const params = {
        reportId:this.reportId
      }
      showTemplateReportData(params).then(res => {
         if (res.code === 0) {
            // 获取服务器 二维码图片

            if (res.data.reportTemplateContext== '' || res.data.reportTemplateContext == null) {
               this.rawHtml = '<h2 style="text-align: center;">暂无内容,请登录web客户端进行编辑修改</h2>';
            } else {
               this.rawHtml = '<html><body>' + res.data.reportTemplateContext + '</body></html>';
               this.templateName = res.data.templateName;
                let qrcodePath = res.data.isResolveQrcodePath;
                if(qrcodePath != '' && qrcodePath != undefined){
                  let Base64 = require('js-base64').Base64;
                  this.qrcodeImageServerPath = this.showImageServerInterfacePath + Base64.encode(qrcodePath+'');
                  this.rawHtml += 
                  '            <div  style="text-align: center;margin-top:200px">\n' +
                  '               <img src="'+ this.qrcodeImageServerPath +'" alt="暂无二维码!"/>\n' +
                  '            </div>\n';
                }

            }

          }
      })
    },
 },
}
</script>

<style scoped>
  .span_font {
    font-size: 18px;
  }

  .select_div {
    margin-left: 10px;
    margin-bottom: 14px;
  }

  .content-view {
    /* margin-left: 10px;
    margin-right: 10px;
    padding: 15px; */
    background: #fff;
  }
</style>
