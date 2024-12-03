<template>
  <div>
    <!-- 选择下一步参与者 -->
    <el-card style="height: auto;" class="marginTopCard box-card" shadow="never">
      <div slot="header" class="clearfix item">
        <span><i class="el-icon-s-order" style="margin-right: 20px;"><b>选择办理</b></i></span>
      </div>
      <FormFooterRun ref="formFooterRun"
                    :processJson="processParam"
                    @show-dialog-and-device="showDialogAndDevice"
                    @show-report-select="showReportSelect">
      </FormFooterRun>
    </el-card>
  </div>
</template>

<script>
  import FormFooterRun from '@/views/core/common/footer-run/FormFooterRun'

  export default {
    name: "processUrl",
    components: {
      FormFooterRun
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
        for (var i = 0; i < processArr.length; i++) {
          var tempArr = processArr[i].split("=")
          this.processParam += '\"' + tempArr[0] + '\":\"' + tempArr[1] + '\",'
        }
        this.processParam = this.processParam.substring(0, this.processParam.length - 1) + '}';
      },
      submitProcessData() {
        return new Promise((resolve, reject) => {
          this.$refs.formFooterRun.submitProcessData().then((val) => {
            resolve(val)
          })
        })
      },
      showDialogAndDevice(flag) {
        this.$emit('show-dialog-and-device-template' , flag);
      },
      //判断当前节点是否为最后一步
      showReportSelect() {
        this.$emit('is-end-node');
      }
    },
    created() {

    },
    props: ['processData'],
    mounted: function() {
      this.startProcess();
    },
  }
</script>

<style>
</style>
