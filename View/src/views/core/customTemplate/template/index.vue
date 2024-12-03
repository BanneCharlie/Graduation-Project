<template>
  <div class="dashboard-editor-container">
    <el-row :gutter="20" style="margin: 5px">
      <el-row>
        <el-col :span="1.5">
          <el-button
            type="success"
            size="mini"
            @click="handleSaveValue"
            style="margin-left: 25px"
          >
            保存模板
          </el-button>
        </el-col>
      </el-row>
    </el-row>
    <span class="span_font" style="margin-left: 41px">字段名称：</span>
    <el-select
      size="mini"
      :disabled="true"
      v-model="fieldName"
      style="width: 200px"
    >
      <el-option
        v-for="(item, index) in fieldList"
        :value="item"
        :key="index"
        >{{ item }}</el-option
      >
    </el-select>

    <el-button
      size="mini"
      type="primary"
      @click="handleValueBtn"
      style="margin-left: 20px; float: right"
    >
      添加字段
    </el-button>
    <!-- 富文本实例 -->
    <vue-ueditor-wrap
      @ready="ready"
      v-model="value"
      :config="myConfig"
      @before-init="configCustomizationEvent"
    ></vue-ueditor-wrap>
  </div>
</template>

<script>
import VueUeditorWrap from "vue-ueditor-wrap";
import { getField } from "@/api/customTemplate/dataSourceConifg";
import {
  getById,
  saveContent,
  changeNotice,
  currentUserNoticeStatus,
} from "@/api/customTemplate/reportTemplate";
import "../../../../../public/static/jquery-1.11.3/jquery.min.js";

import {
  getReportInstace,
  saveReportTemplate,
} from "@/api/business/deviceTask";

export default {
  data() {
    return {
      open: false,
      title: "处理数据",
      showIHave: false,
      showIHaveDisabled: 3,
      contextIsUpdate: false,
      // 默认激活的 tabs name
      activeName: "tab1",
      // 报告内容是空的
      reportContentIsEmpty: false,
      // 存放当前富文本的所有内容
      storeAllDocumentArray: [],

      // 是否是特殊表格 - 即所有内容都是存在于一个table中
      handleSpecialTableMarks: false,
      // 判断当前模板html 表格中是否发生了递归的现象
      judgeTriggeRecursion: false,
      // 存放所有表格的html map , 便于后边更新dom
      storeAllTableDocumentMap: {},

      // 当前所在标签组索引
      currentTabsIndex: null,

      currentContextText: "",
      currentContextHtml: null,
      currentContextTrsHtml: null,
      // 按照表格分组 ，每次处理一张表格
      currentContextColumn: [],
      currentContextTableData: [],
      // 报告书Id
      reportId: "",

      spinShow: false,
      editor: null,
      templateId: "",
      value: "",
      tableList: [],
      fieldList: [],
      fieldName: "",
      isTableEdit: true,
      isCount: false,

      myConfig: {
        // 编辑器不自动被内容撑高
        autoHeightEnabled: false,
        // 初始容器高度
        initialFrameHeight: 450,
        // 初始容器宽度
        initialFrameWidth: "100%",
        //启用自动保存
        enableAutoSave: true,
        //自动保存间隔时间， 单位ms
        saveInterval: 500,
      },
    };
  },
  components: {
    VueUeditorWrap,
  },
  methods: {
    ready(editorInstance) {
      this.editor = editorInstance;
      this.value = this.editor.execCommand("drafts");
      if (this.reportId == undefined || this.reportId == "") {
        getById(this.templateId).then((response) => {
          let templateContent = response.data.templateContent;
          if (!templateContent) {
            this.reportContentIsEmpty = true;
            // this.$message.error('当前报告列表没有内容!')
            return;
          }
          this.editor.setContent(templateContent);
        });
        return;
      }
      getReportInstace(this.reportId).then((response) => {
        this.editor.setContent(response.data.reportTemplateContext);
      });
    },
    // 配置定制事件操作
    // editorId 当前文本框实例Id
    configCustomizationEvent(editorId) {
      this.addUserUploadFileButton(editorId);
    },

    // 添加当前用户富文本上传图片组件按钮事件
    addUserUploadFileButton(editorId) {
      window.UE.registerUI(
        "uploadFile",
        (editor, uiName) => {
          // 注册按钮执行时的 command 命令，使用命令默认就会带有回退操作
          editor.registerCommand(uiName, {
            execCommand() {
              editor.execCommand(
                "inserthtml",
                `<span>这是一段由自定义按钮添加的文字</span>`
              );
            },
          });

          // 创建一个 button
          const btn = new window.UE.ui.Button({
            // 按钮的名字
            name: uiName,
            // 提示
            title: "上传文件/查看历史文件",
            // 需要添加的额外样式，可指定 icon 图标
            cssRules: `background-image: url('${this.UEDITOR_HOME_URL}custom/my-custom-button.png') !important; background-size: cover;`,
            // 点击时执行的命令
            onclick() {
              // 这里可以不用执行命令，做你自己的操作也可
              editor.execCommand(uiName);
            },
          });

          // 当点到编辑内容上时，按钮要做的状态反射
          editor.addListener("selectionchange", function () {
            const state = editor.queryCommandState(uiName);
            if (state === -1) {
              btn.setDisabled(true);
              btn.setChecked(false);
            } else {
              btn.setDisabled(false);
              btn.setChecked(state);
            }
          });

          // 因为你是添加 button，所以需要返回这个 button
          return btn;
        },
        0 /* 指定添加到工具栏上的哪个位置，默认时追加到最后 */,
        editorId /* 指定这个 UI 是哪个编辑器实例上的，默认是页面上所有的编辑器都会添加这个按钮 */
      );
    },
    // ==================================================================
    // ==== 富文本相关 ===
    // ==================================================================

    findField() {
      getField().then((response) => {
        if (response.code === 200) {
          this.fieldList = response.data;
        }
      });
    },
    handleValueBtn() {
      if (this.fieldName == "") {
        this.$message.error("请先选择字段名称");
        return;
      }
      var entity = "";
      if (!this.isCount) {
        entity = "${" + this.fieldName + "}";
      } else {
        entity = "${" + this.fieldName + "}";
      }
      this.editor.execCommand("inserthtml", entity);
    },
    handleSaveValue() {
      if (!this.editor.hasContents()) {
        this.$message.error("模板暂无内容");
      } else {
        if (this.reportId == undefined || this.reportId == "") {
          saveContent({
            content: this.editor.getContent(),
            templateId: this.templateId,
          }).then((response) => {
            if (response.code === 200) {
              this.$message.success("保存成功");
              this.editor.execCommand("clearlocaldata");
              this.$store.state.tagsView.visitedViews.splice(
                this.$store.state.tagsView.visitedViews.findIndex(
                  (item) => item.path === this.$route.path
                ),
                1
              );
              this.$router.push(
                this.$store.state.tagsView.visitedViews[
                  this.$store.state.tagsView.visitedViews.length - 1
                ].path
              );
            }
          });
          return;
        }

        saveReportTemplate({
          reportContent: this.editor.getContent(),
          reportId: this.reportId,
        }).then((response) => {
          if (response.data) {
            this.$message.success("保存成功");
            this.editor.execCommand("clearlocaldata");
            this.$store.state.tagsView.visitedViews.splice(
              this.$store.state.tagsView.visitedViews.findIndex(
                (item) => item.path === this.$route.path
              ),
              1
            );
            this.$router.push(
              this.$store.state.tagsView.visitedViews[
                this.$store.state.tagsView.visitedViews.length - 1
              ].path
            );
          }
        });
      }
    },
    // ============= methods end ================
  },
  mounted() {
    this.dataSourceId = this.$route.params.dataSourceId;
    this.templateId = this.$route.params.templateId;
    this.reportId = this.$route.params.reportId;
    this.findField();
  },
};
</script>


</script>

<style>
.span_font {
  font-size: 18px;
}

.select_div {
  margin-left: 10px;
  margin-bottom: 14px;
}

li {
  margin-top: 10px;
}
</style>
