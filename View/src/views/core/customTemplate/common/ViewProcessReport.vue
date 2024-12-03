<template>
  <div >
    <div class="container-view">
      <div class="menu"   style="display: none"  editor-component="menu">
        <div class="menu-item">
          <div class="menu-item__undo">
            <i></i>
          </div>
          <div class="menu-item__redo">
            <i></i>
          </div>
          <div class="menu-item__painter" title="格式刷(双击可连续使用)">
            <i></i>
          </div>
          <div class="menu-item__format" title="清除格式">
            <i></i>
          </div>
        </div>
        <div class="menu-divider"></div>
        <div class="menu-item">
          <div class="menu-item__font">
            <span class="select" title="字体">微软雅黑</span>
            <div class="options">
              <ul>
                <li data-family="Microsoft YaHei" style="font-family:'Microsoft YaHei';">微软雅黑</li>
                <li data-family="华文宋体" style="font-family:'华文宋体';">华文宋体</li>
                <li data-family="华文黑体" style="font-family:'华文黑体';">华文黑体</li>
                <li data-family="华文仿宋" style="font-family:'华文仿宋';">华文仿宋</li>
                <li data-family="华文楷体" style="font-family:'华文楷体';">华文楷体</li>
                <li data-family="华文琥珀" style="font-family:'华文琥珀';">华文琥珀</li>
                <li data-family="华文楷体" style="font-family:'华文楷体';">华文楷体</li>
                <li data-family="华文隶书" style="font-family:'华文隶书';">华文隶书</li>
                <li data-family="华文新魏" style="font-family:'华文新魏';">华文新魏</li>
                <li data-family="华文行楷" style="font-family:'华文行楷';">华文行楷</li>
                <li data-family="华文中宋" style="font-family:'华文中宋';">华文中宋</li>
                <li data-family="华文彩云" style="font-family:'华文彩云';">华文彩云</li>
                <li data-family="Arial" style="font-family:'Arial';">Arial</li>
                <li data-family="Segoe UI" style="font-family:'Segoe UI';">Segoe UI</li>
                <li data-family="Ink Free" style="font-family:'Ink Free';">Ink Free</li>
                <li data-family="Fantasy" style="font-family:'Fantasy';">Fantasy</li>
                <li data-family="宋体" style="font-family: 宋体">宋体</li>
              </ul>
            </div>
          </div>
          <div class="menu-item__size">
            <span class="select" title="字体">小四</span>
            <div class="options">
              <ul>
                <li data-size="56">初号</li>
                <li data-size="48">小初</li>
                <li data-size="34">一号</li>
                <li data-size="32">小一</li>
                <li data-size="29">二号</li>
                <li data-size="24">小二</li>
                <li data-size="21">三号</li>
                <li data-size="20">小三</li>
                <li data-size="18">四号</li>
                <li data-size="16">小四</li>
                <li data-size="14">五号</li>
                <li data-size="12">小五</li>
                <li data-size="10">六号</li>
                <li data-size="8">小六</li>
                <li data-size="7">七号</li>
                <li data-size="6">八号</li>
              </ul>
            </div>
          </div>
          <div class="menu-item__size-add">
            <i></i>
          </div>
          <div class="menu-item__size-minus">
            <i></i>
          </div>
          <div class="menu-item__bold">
            <i></i>
          </div>
          <!-- 下划线 TODO -->
          <div class="menu-item__italic">
            <i></i>
          </div>

          <div class="menu-item__underline">
            <i></i>
            <span class="select"></span>
            <div class="options">
              <ul>
                <li data-decoration-style='solid'>
                  <i></i>
                </li>
                <li data-decoration-style='double'>
                  <i></i>
                </li>
                <li data-decoration-style='dashed'>
                  <i></i>
                </li>
                <li data-decoration-style='dotted'>
                  <i></i>
                </li>
                <li data-decoration-style='wavy'>
                  <i></i>
                </li>
              </ul>
            </div>
          </div>
          <div class="menu-item__strikeout" title="删除线(Ctrl+Shift+X)">
            <i></i>
          </div>
          <div class="menu-item__superscript">
            <i></i>
          </div>
          <div class="menu-item__subscript">
            <i></i>
          </div>
          <div class="menu-item__color" title="字体颜色">
            <i></i>
            <span></span>
            <input type="color" id="color" />
          </div>
          <div class="menu-item__highlight" title="高亮">
            <i></i>
            <span></span>
            <input type="color" id="highlight">
          </div>
        </div>
        <div class="menu-divider"></div>
        <div class="menu-item">
          <div class="menu-item__title">
            <i></i>
            <span class="select" title="切换标题">正文</span>
            <div class="options">
              <ul>
                <li style="font-size:16px;">正文</li>
                <li data-level="first" style="font-size:26px;">标题1</li>
                <li data-level="second" style="font-size:24px;">标题2</li>
                <li data-level="third" style="font-size:22px;">标题3</li>
                <li data-level="fourth" style="font-size:20px;">标题4</li>
                <li data-level="fifth" style="font-size:18px;">标题5</li>
                <li data-level="sixth" style="font-size:16px;">标题6</li>
              </ul>
            </div>
          </div>
          <div class="menu-item__left">
            <i></i>
          </div>
          <div class="menu-item__center">
            <i></i>
          </div>
          <div class="menu-item__right">
            <i></i>
          </div>
          <div class="menu-item__alignment">
            <i></i>
          </div>
          <div class="menu-item__justify">
            <i></i>
          </div>
          <div class="menu-item__row-margin">
            <i title="行间距"></i>
            <div class="options">
              <ul>
                <li data-rowmargin='1'>1</li>
                <li data-rowmargin="1.25">1.25</li>
                <li data-rowmargin="1.5">1.5</li>
                <li data-rowmargin="1.75">1.75</li>
                <li data-rowmargin="2">2</li>
                <li data-rowmargin="2.5">2.5</li>
                <li data-rowmargin="3">3</li>
              </ul>
            </div>
          </div>
          <div class="menu-item__list">
            <i></i>
            <div class="options">
              <ul>
                <li>
                  <label>取消列表</label>
                </li>
                <li data-list-type="ol" data-list-style='decimal'>
                  <label>有序列表：</label>
                  <ol>
                    <li>________</li>
                  </ol>
                </li>
                <li data-list-type="ul" data-list-style='checkbox'>
                  <label>复选框列表：</label>
                  <ul style="list-style-type: '☑️ ';">
                    <li>________</li>
                  </ul>
                </li>
                <li data-list-type="ul" data-list-style='disc'>
                  <label>实心圆点列表：</label>
                  <ul style="list-style-type: disc;">
                    <li>________</li>
                  </ul>
                </li>
                <li data-list-type="ul" data-list-style='circle'>
                  <label>空心圆点列表：</label>
                  <ul style="list-style-type: circle;">
                    <li>________</li>
                  </ul>
                </li>
                <li data-list-type="ul" data-list-style='square'>
                  <label>空心方块列表：</label>
                  <ul style="list-style-type: square;">
                    <li>________</li>
                  </ul>
                </li>
              </ul>
            </div>
          </div>
        </div>
        <div class="menu-divider"></div>
        <div class="menu-item">
          <div class="menu-item__table">
            <i title="表格"></i>
          </div>
          <div class="menu-item__table__collapse">
            <div class="table-close">×</div>
            <div class="table-title">
              <span class="table-select">插入</span>
              <span>表格</span>
            </div>
            <div class="table-panel"></div>
          </div>
          <div class="menu-item__image">
            <i title="图片"></i>
            <input type="file" id="image" accept=".png, .jpg, .jpeg, .svg, .gif">
          </div>
          <div class="menu-item__hyperlink" style="display: none">
            <i title="超链接"></i>
          </div>

          <div class="menu-item__separator">
            <i title="分割线"></i>
            <div class="options">
              <ul>
                <li data-separator='0,0'>
                  <i></i>
                </li>
                <li data-separator="1,1">
                  <i></i>
                </li>
                <li data-separator="3,1">
                  <i></i>
                </li>
                <li data-separator="4,4">
                  <i></i>
                </li>
                <li data-separator="7,3,3,3">
                  <i></i>
                </li>
                <li data-separator="6,2,2,2,2,2">
                  <i></i>
                </li>
              </ul>
            </div>
          </div>

          <div class="menu-item__watermark" style="display: none">
            <i title="水印(添加、删除)"></i>
            <div class="options">
              <ul>
                <li data-menu="add">添加水印</li>
                <li data-menu="delete">删除水印</li>
              </ul>
            </div>
          </div>
          <div class="menu-item__codeblock" title="代码块" style="display: none">
            <i></i>
          </div>
          <div class="menu-item__page-break" title="分页符" style="display: none">
            <i></i>
          </div>

          <div class="menu-item__control" style="display: none">
            <i title="控件"></i>
            <div class="options">
              <ul>
                <li data-control='text'>文本</li>
                <li data-control="select">列举</li>
                <li data-control="date">日期</li>
                <li data-control="checkbox">复选框</li>
                <li data-control="radio">单选框</li>
              </ul>
            </div>
          </div>

          <div class="menu-item__checkbox" title="复选框">
            <i></i>
          </div>
          <div class="menu-item__radio" title="单选框">
            <i></i>
          </div>
          <div class="menu-item__latex" title="LateX" style="display: none">
            <i></i>
          </div>
          <div class="menu-item__date">
            <i title="日期"></i>
            <div class="options">
              <ul>
                <li data-format="yyyy-MM-dd"></li>
                <li data-format="yyyy-MM-dd hh:mm:ss"></li>
              </ul>
            </div>
          </div>
          <div class="menu-item__block" title="内容块" style="display: none">
            <i></i>
          </div>
        </div>
        <div class="menu-divider"></div>
        <div class="menu-item">
          <div class="menu-item__search" data-menu="search">
            <i></i>
          </div>
          <div class="menu-item__search__collapse" data-menu="search">
            <div class="menu-item__search__collapse__search">
              <input type="text" />
              <label class="search-result"></label>
              <div class="arrow-left">
                <i></i>
              </div>
              <div class="arrow-right">
                <i></i>
              </div>
              <span>×</span>
            </div>
            <div class="menu-item__search__collapse__replace">
              <input type="text">
              <button>替换</button>
            </div>
          </div>
          <div class="menu-item__print" data-menu="print">
            <i></i>
          </div>
        </div>
      </div  >
      <div class="catalog" editor-component="catalog"  style="display: none">
        <div class="catalog__header">
          <span>目录</span>
          <div class="catalog__header__close">
            <i></i>
          </div>
        </div>
        <div class="catalog__main"></div>
      </div>

      <div class="canvas-editor editor"></div>

      <div class="comment" editor-component="comment"></div>

      <div class="footer"  style="display: none" editor-component="footer">
        <div>
          <div class="catalog-mode" title="目录">
            <i></i>
          </div>
          <div class="page-mode">
            <i title="页面模式(分页、连页)"></i>
            <div class="options">
              <ul>
                <li data-page-mode="paging" class="active">分页</li>
                <li data-page-mode="continuity">连页</li>
              </ul>
            </div>
          </div>
          <span>可见页码：<span class="page-no-list">1</span></span>
          <span>页面：<span class="page-no">1</span>/<span class="page-size">1</span></span>
          <span>字数：<span class="word-count">0</span></span>
        </div>
        <div class="editor-mode" title="编辑模式(编辑、清洁、只读、表单)">编辑模式</div>
        <div>
          <div class="page-scale-minus" title="缩小(Ctrl+-)">
            <i></i>
          </div>
          <span class="page-scale-percentage" title="显示比例(点击可复原Ctrl+0)">100%</span>
          <div class="page-scale-add" title="放大(Ctrl+=)">
            <i></i>
          </div>
          <div class="paper-size">
            <i title="纸张类型"></i>
            <div class="options">
              <ul>
                <li data-paper-size="794*1123" class="active">A4</li>
                <li data-paper-size="1593*2251">A2</li>
                <li data-paper-size="1125*1593">A3</li>
                <li data-paper-size="565*796">A5</li>
                <li data-paper-size="412*488">5号信封</li>
                <li data-paper-size="450*866">6号信封</li>
                <li data-paper-size="609*862">7号信封</li>
                <li data-paper-size="862*1221">9号信封</li>
                <li data-paper-size="813*1266">法律用纸</li>
                <li data-paper-size="813*1054">信纸</li>
              </ul>
            </div>
          </div>
          <div class="paper-direction">
            <i title="纸张方向"></i>
            <div class="options">
              <ul>
                <li data-paper-direction="vertical" class="active">纵向</li>
                <li data-paper-direction="horizontal">横向</li>
              </ul>
            </div>
          </div>
          <div class="paper-margin" title="页边距">
            <i></i>
          </div>
          <div class="fullscreen" title="全屏显示">
            <i></i>
          </div>
          <div class="editor-option" title="编辑器设置">
            <i></i>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// 滚动条插件
import '../../../../../public/static/jquery-1.11.3/jquery.min';
import Plugins from '../../../../../public/static/scrollPlugins/abScrollBar1.2.source';

// API .......
import { getReportInstace } from '@/api/business/deviceTask';

import { Init } from '@/views/core/customTemplate/canvaseditor/canvas'
import { EditorMode } from '@hufe921/canvas-editor'

// 网关 api 地址
const hostName = window.location.hostname;
const api_pattern = hostName == 'localhost' ? 'dev-api' : 'prod-api';
const proxyApiUrl = location.origin + '/' + api_pattern;
const requestDataSourceApi = '/api/report';
// ================  常量  ================
const INNER_TARGET_TAG_GROUP_CLASS_NAME_ARR = ['compositionTypeBianZhi', 'compositionTypeShenHe', 'compositionTypePiZhun'];

export default {
  components: {
  },
  data() {
    return {
      // 当前报告的html内容
      rawHtml: null,

      instance: null,
      genericContent:null
    };
  },
  props: {
    reportId: {
      type: String,
      default: null
    }
  },
  created() {
  },
  watch: {
    reportId: {
      handler(newVal, oldVal) {
        if (newVal) {
          this.loadReportInstance();
        }
      }
    }
  },
  mounted() {
  },
  methods: {
    // 初始化插件
    initialScrollPluginsConfig() {
      let containerWidth = $('.abScrollBar-box3').width();
      let containerHeight = $('.abScrollBar-box3').height();
      $(function () {
        Plugins.abScrollBar({
          scrollType: 'y' /* 默认=y, x=水平方向滑动, y=垂直方向滑动 */,
          contentEl: '.htmlPreviewMain' /* 滚动内容层 */,
          barClass: 'bar3' /* 自定义的滚动条样式 */,
          barBgClass: 'bar3-bg' /* 滚动条背景样式 */,
          width: containerWidth /* 滚动内容宽度，此例子宽度等于abScrollBar-box层的宽度 */,
          height: containerHeight /* 滚动内容高度，此例子高度等于abScrollBar-box层的高度 */,
          barWidth: 8 /* 默认6, 滚动条宽度 */,
          isHideBar: false /* 当鼠标离开容器区域，默认为 false 时显示滚动条，为true时隐藏滚动条 */,
          mousewheelStep: 30 /* 默认=30，鼠标轮滚步长 */
        });
      });
    },

    // 表单提交 ------------- 表单数据方法
    loadReportInstance() {
      getReportInstace(this.reportId).then(response => {
        if ((response.data.reportGenericContext) !== null && (response.data.reportGenericContext) !== '' && (response.data.reportGenericContext).includes('header')) {
          let jsonString = response.data.reportGenericContext;
          let reportGenericContent = JSON.parse(jsonString);
          this.genericContent = reportGenericContent.data;
        } else {
          this.genericContent = {
            header: [],
            main: [
              {
                value: "暂无数据",
                size: 40,
                bold: true,
                color: "rgb(33, 53, 71)",
                italic: false
              }
            ],
            footer: []
          };
        }
        let instance = Init(this.genericContent);
        instance.instance.command.executeMode(EditorMode.READONLY);
      });
    },

    // 设置当前报告的html内容
    initReportInfo() {
      $('.htmlPreviewMain').append($(this.rawHtml));
      this.getReportExamineProcessHistoryInfo();
      this.initialScrollPluginsConfig();
    },

    // 处理报告 审批人签名、日期等信息

    /** 获取当前报告审批流程历史信息 */
    getReportExamineProcessHistoryInfo() {
      $.ajax({
        url: proxyApiUrl + requestDataSourceApi + '/get-report-process-info/' + this.reportId,
        method: 'get',
        contentType: 'application/json',
        async: false,
        success: res => {
          if (res.data) {
            this.executeReportExamineProcessInfo(res.data);
          }
        }
      });
    },

    /** 处理报告审批流程信息 */
    executeReportExamineProcessInfo(reportExamineProcessMap) {
      for (const key in reportExamineProcessMap) {
        let className;
        let classIndex = -1;
        // 申请
        if (key.indexOf('申请') != -1) {
          className = INNER_TARGET_TAG_GROUP_CLASS_NAME_ARR[0];
          classIndex = 0;
        } else if (key.indexOf('审核') != -1) {
          className = INNER_TARGET_TAG_GROUP_CLASS_NAME_ARR[1];
          classIndex = 1;
        } else if (key.indexOf('批准') != -1) {
          className = INNER_TARGET_TAG_GROUP_CLASS_NAME_ARR[2];
          classIndex = 2;
        }

        if (!!className) {
          // ExamineUserPartInfo
          const value = reportExamineProcessMap[key];
          this.insertExamineUserPartInfo(classIndex, className, value);
        }
      }
    },

    /** 插入审批人的签名信息 */
    insertExamineUserPartInfo(classIndex, targetClassName, examineUserPart) {
      // 目标标签
      let targetTag = $('.' + targetClassName);

      // 插入签名
      let signatureId = examineUserPart['signatureId'];
      if (signatureId) {
        let signatureUrl = proxyApiUrl + '/fileModule/show-signature/' + signatureId;
        let insertExamineUserSignatureTag = '<span><img style="height:25px;" src="' + signatureUrl + '" /></span>';
        targetTag.append($(insertExamineUserSignatureTag));
      } else {
        let insertExamineUserSignatureTag = '<span><img style="height:25px;" alt="暂无签名"" /></span>';
        targetTag.append($(insertExamineUserSignatureTag));
      }

      // 目标标签的下一个目标标签 (日期标签)
      let nextTargetTag = $(targetTag[0].nextElementSibling);

      // 插入日期标签信息 - 此时先插入 input 标签可用于更改 , 最后执行打印时 替换为内容
      let insertExamineUserDateTag = '<span>' + examineUserPart['executeTime'] + '</span>';
      // let insertExamineUserDateTag =
      // 	'<input style="width:130px;" class="' +
      // 	INNER_TARGET_TAG_DATE_GROUP_CLASS_NAME_ARR[classIndex] +
      // 	'" type="date" value="' +
      // 	examineUserPart['executeTime'] +
      // 	'" />';
      nextTargetTag.append($(insertExamineUserDateTag));
    }

    // ===== methods end ==========
  }
};
</script>

<style>

@import url("../canvaseditor/style.css");

</style>

<style>
.abScrollBar-box3 .htmlPreviewMain {
  color: #555;
  padding: 0px 30px;
  left: 20% !important;
}
/* 自定义的滚动条样式 */
.abScrollBar-box3 .bar3-bg {
  background: #fafafa;
}
.abScrollBar-box3 .bar3 {
  background: #4ad585;
}


.container-view {
  width: 100%;
  height: calc(100vh - 120px);
  overflow-y: scroll;
}

.footer {
  position: static;
  background-color: #f2f4f7;
  text-align: center;
  line-height: 30px!important; /* 调整footer高度 */
  display: none;
}

@media (max-width: 1220px) {
  .menu {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    justify-content: flex-start;
  }
}

</style>
