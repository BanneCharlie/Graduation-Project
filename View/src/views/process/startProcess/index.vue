<template>
  <div>
    <!-- 选择下一步参与者 -->
    <el-card style="height: 200px;" class="box-card" shadow="never">
      <div slot="header" class="clearfix item" >
        <span><i class="el-icon-s-order" style="margin-right: 20px;"><b >选择办理</b></i></span>
      </div>
      <FormFooter ref="formFooter" :processJson="processParam"></FormFooter>
    </el-card>
  </div>
</template>

<script>
  import FormFooter from '@/views/core/common/footer/FormFooter'

  export default {
     name: "processUrl",
     components:{
       FormFooter
     },
     data() {
       return {
          processParam: '',
       }
     },
     methods: {
      startProcess() {
        this.processParam = '{'
        var processArr = this.processData.split("&");
        for(var i = 0; i < processArr.length; i++) {
          var tempArr = processArr[i].split("=")
          this.processParam += '\"' + tempArr[0] + '\":\"' + tempArr[1] + '\",'
        }
        this.processParam = this.processParam.substring(0, this.processParam.length - 1) + '}';

      },
       submitProcessData() {
         return new Promise((resolve,reject)=>{
           this.$refs.formFooter.submitProcessData().then((val) => {
             resolve(val)
           })
         })
      }
     },
     created() {

     },
    watch: {
    },
     props: ['processData'],
     mounted: function() {
        this.startProcess();
     },
  }
</script>

<style>
</style>
