<template>
  <div class="dashboard-editor-container">

    <CanvasEditor ref="canvasEditor" :genericContentId="genericContentId"  :single="single"  @save-content="handleSaveContent"></CanvasEditor>


    <!-- 展示当前用户的所有上传文件记录 -->
    <UserUploadHistoryComponet
      v-if="userUploadHistioryDialog"
      @selectAttach="clickSelectAttach"
      @closeComponent="closeComponentMethod"
    >
    </UserUploadHistoryComponet>
    <!-- tmeplate end -->
  </div>
</template>

<script>
import CanvasEditor from '@/views/core/customTemplate/canvaseditor/index.vue'
import UserUploadHistoryComponet from "@/views/core/customTemplate/common/UserUploadHistoryComponent";

import { getField } from "@/api/customTemplate/dataSourceConifg";
import { currentUserNoticeStatus,getById, saveGenericContent } from "@/api/customTemplate/reportTemplate";

import { getReportInstace, saveReportTemplate, } from "@/api/business/deviceTask";

export default {
  components: {
    CanvasEditor,
    UserUploadHistoryComponet,
  },
  data() {
    return {
      canvasEditor:null,
      // 文本内容
      genericContentId:null,
      // 传递的数据
      content:null,
      contentHtml:null,
      // 标识符号
      single: "view",

      loading: null,
      showComponent: true,
      // 电子签名
      insertSignatureFlag: false,
      showIHave: false,
      showIHaveDisabled: 3,
      // ----
      spinShow: false,
      editor: null,
      dataSourceId: "",
      templateId: "",
      reportId: "",

      value: "",
      fieldMapList: [],
      fieldName: "",

      selectTagVal: "",
      tagSelectItem: [
        { tagName: "检验报告审核", tagVal: "tag_jybgsh", isTagDisabled: false },
        { tagName: "检验报告批准", tagVal: "tag_jybgpz", isTagDisabled: false },
      ],
      tagSelectFlag: {
        tag_jybgsh: false,
        tag_jybgpz: false,
      },

      isTableEdit: true,
      isStart: true,
      editorIsFocurs: false, // 编辑器是否处在焦点状态
      changeBeforeFocurs: false, // 在改变之前的焦点标志位
      isCount: false,

      // 当前请求服务器地址url
      nowHttpAddress: null,
      myConfig: {
        UEDITOR_HOME_URL: "/static/UEditor/",
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

      // 展示用户上传历史dialog
      userUploadHistioryDialog: false,
      // ==== field end ====
    };
  },
  watch: {},
  created() {
    this.reportId = this.$route.query.reportId ? this.$route.query.reportId : this.$route.params.reportId;
    this.genericContentId = this.reportId;
  },
  mounted(){
    this.findField();
    this.getLoginNoticeStatus();
  },
  methods: {
    ready(editorInstance) {
      this.editor = editorInstance;
      this.editor.addListener("focus", () => {
        this.$nextTick(() => {
          this.isStart = false;
          this.editorIsFocurs = true;
        });
      });
      this.editor.addListener("blur", () => {
        this.$nextTick(() => {
          this.editorIsFocurs = false;
        });
      });
      this.executeLoadingPage();
      this.value = this.editor.execCommand("drafts");
      // 不是报告 - 而是模板
      if (!this.reportId) {
        getById(this.templateId).then((response) => {
          let html = response.data.templateGenericContent;
          this.editor.setContent(!html ? '<h1 style="text-align:center;border:2px solid;">暂无内容!</h1>' : html);
          setTimeout(() => {
            this.loading.close();
          }, 800);
        });
        return;
      }
      // 获取报告信息 - 而不是模板信息
      getReportInstace(this.reportId).then((response) => {
        let html = response.data.reportGenericContext;
        this.editor.setContent(!html ? '<h1 style="text-align:center;border:2px solid;">暂无内容!</h1>' : html);
        setTimeout(() => {
          this.loading.close();
        }, 800);
      });
    },
    // 每次提交流程都在该流程上添加笼罩层
    executeLoadingPage(message) {
      let setMessage = '请稍候......';
      this.loading = this.$loading({
        lock: true,
        text: setMessage,
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
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
          // 创建一个 button
          const btn = new window.UE.ui.Button({
            // 按钮的名字
            name: uiName,
            // 提示
            title: "上传文件/查看历史文件",
            // 需要添加的额外样式，可指定 icon 图标
            cssRules: `background-image: url('${this.myConfig.UEDITOR_HOME_URL}custom/logo.png') !important; background-size: cover;`,
            // 点击时执行的命令
            onclick: () => {
              this.clickShowUserUploadHistoryEventMethod();
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
        2 /* 指定添加到工具栏上的哪个位置，默认时追加到最后 */,
        editorId /* 指定这个 UI 是哪个编辑器实例上的，默认是页面上所有的编辑器都会添加这个按钮 */
      );
    },

    // =============================================
    // ============  当前用户上传的附件历史 =========
    // =============================================
    // 1. 点击查看历史上传文件组件
    clickShowUserUploadHistoryEventMethod() {
      this.userUploadHistioryDialog = true;
    },
    // 点击选择附件信息插入html内容
    clickSelectAttach(resultHtml) {
      if (!resultHtml) {
        this.$message.error("当前仅支持加入图片类型附件!");
        return;
      }
      if (!this.isStart && !this.editorIsFocurs) {
        this.editor.execCommand("inserthtml", resultHtml);
        this.$message.success('插入图片成功!');
        this.closeComponentMethod();
      } else {
        this.$message.error("您当还未选中需要标记的区域!");
        return;
      }
    },
    closeComponentMethod() {
      this.userUploadHistioryDialog = false;
    },
    // ==================================================================
    // =========================== 富文本相关 ===========================
    // ==================================================================

    // 获取所需字段
    findField() {
      getField().then((response) => {
        if (response.code === 200) {
          this.fieldMapList = response.data;
        }
      });
    },
    // 在 保存模板之前会先检验是否进行标记了
    checkAllTagIsCommit() {
      return true;
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
      // 提交 子组件执行 saveContent() 方法  TODO
      this.$refs.canvasEditor.saveContent();

      if (!this.content) {
        this.$message.error("模板暂无内容");
      } else {
        if (this.reportId == undefined || this.reportId == "") {
          saveGenericContent({
            genericContent: this.contentHtml,
            content: this.content,
            templateId: this.templateId,
          }).then((response) => {
            if (response.code === 200) {
              this.$message.success("模板保存成功");
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

        if (this.checkAllTagIsCommit()) {
          saveReportTemplate({
            reportGeneric: this.editor.getContent(),
            reportId: this.reportId,
          }).then((response) => {
            if (response.data) {
              this.$message.success("报告保存成功");
              this.editor = undefined;
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
      }
    },
    // 返回的数据通过 handleSaveContent接收 TODO
    handleSaveContent(data) {
      const contentToSaveHtml = {
        html: data.html.header + data.html.main + data.html.footer,
      };
      this.contentHtml = JSON.stringify(contentToSaveHtml, null, 1);

      const contentToSaveData = {
        data: data.data
      };
      this.content = JSON.stringify(contentToSaveData, null, 1);
    },

    // 屏蔽通知操作
    getLoginNoticeStatus() {
      currentUserNoticeStatus().then((response) => {
        if (response.data == "0") {
          this.showIHaveDisabled = 3;
          this.showIHave = true;
          let timeInterval = setInterval(() => {
            this.showIHaveDisabled--;
            if (this.showIHaveDisabled == 0) {
              clearInterval(timeInterval);
            }
          }, 1000);
        }
      });
    },
    // ==== methods end ====
  },
};
</script>


<style>
.dashboard-editor-container{
  overflow-y: hidden;
}

.span_font {
  font-size: 14px;
}

.select_div {
  margin-left: 10px;
  margin-bottom: 14px;
}
</style>
