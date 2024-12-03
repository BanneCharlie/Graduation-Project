<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="88px"
      label-suffix=":"
    >
      <el-form-item label="设备档案号" prop="equipmentArchiveNumber">
        <el-input
          v-model="queryParams.equipmentArchiveNumber"
          placeholder="请输入设备档案号"
          size="small"
        />
      </el-form-item>
      <el-form-item label="维保单位" prop="maintenanceUnit">
        <el-input
          v-model="queryParams.maintenanceUnit"
          placeholder="请输入维保单位"
          size="small"
        />
      </el-form-item>
      <el-form-item label="检验员1" prop="inspectorOne">
        <el-input
          v-model="queryParams.inspectorOne"
          placeholder="请输入检验员1"
          size="small"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
    </el-form>
    <el-row>
      <el-col :span="1.5">
        <el-button type="success" size="mini" @click="handleImport">
          导入数据
        </el-button>
      </el-col>
    </el-row>
    <el-table
      max-height="600"
      size="small"
      v-loading="loading"
      :data="queryData"
      border
    >
      <el-table-column
        label="序号"
        type="index"
        align="center"
        width="60"
      ></el-table-column>
      <el-table-column
        label="设备档案号"
        align="center"
        width="150"
        prop="equipmentArchiveNumber"
      ></el-table-column>
      <el-table-column
        label="报告编号"
        align="center"
        width="150"
        prop="reportNumber"
      ></el-table-column>
      <el-table-column
        label="使用单位"
        align="center"
        width="300"
        prop="useUnit"
      ></el-table-column>
      <el-table-column
        label="设备品种"
        align="center"
        width="150"
        prop="equipmentVarieties"
      ></el-table-column>
      <el-table-column
        label="出厂编号"
        align="center"
        width="150"
        prop="manufacturingNumber"
      ></el-table-column>
      <el-table-column
        label="维保单位"
        align="center"
        width="200"
        prop="maintenanceUnit"
      ></el-table-column>
      <el-table-column
        label="检验类型"
        align="center"
        width="150"
        prop="inspectionType"
      ></el-table-column>
      <el-table-column
        label="检验日期"
        align="center"
        width="150"
        prop="examinationDate"
      ></el-table-column>
      <el-table-column
        label="检验结论"
        align="center"
        width="100"
        prop="inspectTheConclusion"
      ></el-table-column>
      <el-table-column
        label="责任检验员"
        align="center"
        width="100"
        prop="responsibilityInspector"
      ></el-table-column>
      <el-table-column
        label="检验员1"
        align="center"
        width="100"
        prop="inspectorOne"
      ></el-table-column>
      <el-table-column
        label="报告文件名"
        align="center"
        width="350"
        prop="reportFileName"
      ></el-table-column>
      <el-table-column label="操作" align="center" width="100" fixed="right">
        <template slot-scope="scope">
          <el-button
            icon="el-icon-download"
            type="text"
            size="mini"
            @click="downloadReport(scope.row)"
          >
            下载报告
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="queryTotal > 0"
      :total="queryTotal"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="handleQuery"
    />

    <!-- 用户导入对话框 -->
    <el-dialog
      :title="upload.title"
      :visible.sync="upload.open"
      width="400px"
      append-to-body
    >
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__tip" slot="tip">
          <el-link type="info" style="font-size: 12px" @click="importTemplate"
            >下载模板</el-link
          >
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getDataList, downloadTemplate } from "@/api/business/proDataLedger";
import { getToken } from "@/utils/auth";

export default {
  components: {},
  props: {},
  data() {
    return {
      loading: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        equipmentArchiveNumber: null,
        maintenanceUnit: null,
        inspectorOne: null,
      },
      queryData: [],
      queryTotal: 0,

      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/prod-data-ledger/importData",
      },
      // ===== field end =====
    };
  },
  created() {},
  watch: {},
  mounted() {
    this.handleQuery();
  },
  methods: {
    handleQuery() {
      this.loading = true;
      getDataList(this.queryParams).then((response) => {
        this.queryData = response.rows;
        this.queryTotal = response.total;
        setTimeout(() => {
          this.loading = false;
        }, 500);
      });
    },

    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        equipmentArchiveNumber: null,
        maintenanceUnit: null,
        inspectorOne: null,
      };
      this.handleQuery();
    },
    // 下载报告
    downloadReport(row) {
      const reportType = row.reportType;
      const reportName = row.reportFileName;
      console.log(reportType);
      if(reportType == 1){
        let year = reportName.substring(0, 4);
        let month = reportName.substring(4, 6);
        let day = reportName.substring(6, 8);

        let resultFilePath =
          "E:/PDF/" + year + "-" + month + "/" + day + "/" + reportName + ".PDF";
        this.downloadFile(resultFilePath, reportName + ".PDF");
      }else if(reportType == 2){
        let resultFilePath = "E:/NEW_PDF/" + reportName;
        this.downloadFile(resultFilePath, reportName);
      }

    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "数据导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      downloadTemplate().then((response) => {
        console.log(response);
        this.download(response.msg);
      });
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert(response.msg, "导入结果", { dangerouslyUseHTMLString: true });
      this.handleQuery();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
  },
};
</script>

<style></style>
