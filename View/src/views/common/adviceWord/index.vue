<template>
  <div>
    <!-- 快捷意见选择框-->
    <el-dialog
      title="请选择"
      :visible.sync="dialogVisible"
      :show-close="false"
      :close-on-click-modal="false"
      width="40%">
      <el-card class="box-card" shadow="never" body-style="border-radius: 0px;">
        <div slot="header" class="clearfix">
          <span>常用词语</span>
        </div>
        <div>
          <el-tag @click="setOpinion('请传阅')" type="success" class="tagWord" effect="dark" color="#54B15D">请传阅</el-tag>
          <el-tag @click="setOpinion('请核稿')" type="success" class="tagWord" effect="dark" color="#54B15D">请核稿</el-tag>
          <el-tag @click="setOpinion('请落实')" type="success" class="tagWord" effect="dark" color="#54B15D">请落实</el-tag>
          <el-tag @click="setOpinion('请签发')" type="success" class="tagWord" effect="dark" color="#54B15D">请签发</el-tag>
          <el-tag @click="setOpinion('请阅办')" type="success" class="tagWord" effect="dark" color="#54B15D">请阅办</el-tag>
        </div>
        <div>
          <el-tag @click="setOpinion('已修改')" type="success" class="tagWord" effect="dark" color="#54B15D">已修改</el-tag>
          <el-tag @click="setOpinion('请阅处')" type="success" class="tagWord" effect="dark" color="#54B15D">请阅处</el-tag>
          <el-tag @click="setOpinion('请阅示')" type="success" class="tagWord" effect="dark" color="#54B15D">请阅示</el-tag>
          <el-tag @click="setOpinion('同意')" type="success" class="tagWord" effect="dark" color="#54B15D">同意</el-tag>
          <el-tag @click="setOpinion('已阅')" type="success" class="tagWord" effect="dark" color="#54B15D">已阅</el-tag>
        </div>
      </el-card>

      <el-card class="box-card" shadow="never">
        <div slot="header" class="clearfix">
          <span>意见</span>
        </div>
        <div>
          <el-input type="textarea" :rows="2" placeholder="请输入内容" v-model="opinion"></el-input>
        </div>
      </el-card>

      <span slot="footer" class="dialog-footer">
        <el-button @click="changeDialog(false)">取 消</el-button>
        <el-button type="primary" @click="confirmOpinion">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>

  export default {
      data() {
        return {
          dialogVisible: false,
          opinion: ''
        };
      },
      methods: {
        //修改弹框的开启状态
        changeDialog(flag) {
          this.dialogVisible = flag;
          this.opinion = '';
        },
        //确认选择的快捷意见
        confirmOpinion() {
          this.dialogVisible = false;
          this.$emit('setQuickOpinion', this.opinion)
        },
        //点击快捷短语设置意见到输入框
        setOpinion(content) {
          this.opinion += content;
        }
      }
    };

</script>

<style>
  .tagWord {
    margin-left: 20px;
    margin-top: 10px;
    cursor: pointer;
  }

</style>
