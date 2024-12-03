<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      v-show="showSearch"
      :inline="true"
    >
      <el-form-item label="标题名称" prop="reportTitle">
        <el-input
          v-model="queryParams.reportTitle"
          placeholder="不能查找一级标题"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="模板内容" prop="templateName">
        <el-input v-model="queryParams.templateName" placeholder="请输入数据源名称" clearable size="small" style="width: 240px"
          @keyup.enter.native="handleQuery" />
      </el-form-item> -->
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
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-circle-plus-outline"
          @click="handleModalAdd"
          >添加</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          :disabled="multiple"
          @click="handleRemove"
          >删除</el-button
        >
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      size="medium"
      @selection-change="handleSelectionChange"
      v-loading="loading"
      row-key="reportRowId"
      :data="data"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
    >
      <el-table-column
        type="selection"
        align="center"
        width="80"
      ></el-table-column>
      <el-table-column
        prop="reportTitle"
        label="标题名称"
        align="left"
        width="360"
      ></el-table-column>
      <el-table-column
        prop="levelRowName"
        label="标题等级"
        align="center"
        width="150"
      ></el-table-column>
      <el-table-column
        prop="parentName"
        label="父标题名称"
        align="center"
        width="250"
      ></el-table-column>
      <el-table-column
        prop="templateName"
        label="模板名称"
        align="center"
        width="150"
      ></el-table-column>
      <el-table-column
        prop="orderNum"
        label="排序"
        align="center"
        width="80"
      ></el-table-column>
      <el-table-column label="操作" align="center" width="200">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="primary"
            icon="el-icon-edit-outline"
            @click="handleModalUpdate(scope.row)"
            >编辑</el-button
          >
          <el-button
            size="mini"
            type="danger"
            icon="el-icon-delete"
            @click="handleRemove(scope.row)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog
      :title="modalTitle"
      :visible.sync="open"
      width="50%"
      center
      append-to-body
    >
      <el-form
        ref="dialogForm"
        label-suffix=":"
        label-width="220px"
        label-position="right"
        size="medium"
        :model="formItem"
        :rules="formItemRules"
      >
        <el-form-item label="选择上级标题" prop="parentId">
          <treeselect
            v-model="formItem.parentId"
            :options="rowOptions"
            :normalizer="normalizer"
            :show-count="true"
            placeholder="选择上级菜单"
            style="width: 350px"
          />
        </el-form-item>
        <el-form-item label="标题名称" prop="reportTitle">
          <el-input
            style="width: 350px"
            v-model="formItem.reportTitle"
            placeholder="如:1.1 二级标题"
          ></el-input>
        </el-form-item>
        <el-form-item label="模板类型" prop="templateType">
          <el-select
            v-model="formItem.levelRowName"
            style="width: 350px"
            @change="changeLavel"
          >
            <el-option
              v-for="item in levels"
              :key="item.value + '_' + item.label"
              :label="item.label"
              :value="item.value + '_' + item.label"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="报告模板" prop="templateName">
          <el-select v-model="formItem.templateName" style="width: 350px" @change="changeTemplate">
            <el-option
              v-for="item in templates"
              :key="item.templateId + '_' + item.templateName"
              :label="item.templateName"
              :value="item.templateId + '_' + item.templateName"
            >
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="检验结果" prop="reportResult">
          <el-input
            v-model="formItem.reportResult"
            placeholder="请输入内容(可空)"
            style="width: 350px"
          ></el-input>
        </el-form-item>
        <el-form-item label="检验结论" prop="reportConclusion">
          <el-input
            v-model="formItem.reportConclusion"
            placeholder="请输入内容(可空)"
            style="width: 350px"
          ></el-input>
        </el-form-item>
        <el-form-item label="排序" prop="orderNum">
          <el-input-number
            v-model="formItem.orderNum"
            :min="0"
            :max="100"
            label="排序"
            style="width: 350px"
          ></el-input-number>
        </el-form-item>
      </el-form>
      <span style="margin-top: 30px" slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">保存</el-button>
        <el-button type="danger" @click="handleReset">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  treeList,
  delReportRow,
  listRow,
  saveReportRow,
  getInfoByRowId,
} from "@/api/customTemplate/reportRow";
import { getAllTemplate } from "@/api/customTemplate/reportTemplate";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "ReportTemplate",
  components: { Treeselect },
  data() {
    return {
      defaultVal: "",
      defaultLabel: "",
      loading: false,
      modalTitle: "",
      spinShow: false,
      isParent: true,
      isEdit: true,
      tableSelection: [],
      levels: [
        {
          value: "1",
          label: "一级标题",
        },
        {
          value: "2",
          label: "二级标题",
        },
        {
          value: "3",
          label: "三级标题",
        },
      ],
      reportRows: [],
      templates: [],
      // 总条数
      total: 0,
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        dataSourceName: undefined,
        createTime: undefined,
      },
      formItemRules: {
        parentId: [
          {
            required: true,
            message: "不能为空",
            trigger: "blur",
          },
        ],
        reportTitle: [
          {
            required: true,
            message: "不能为空",
            trigger: "blur",
          },
        ],
        templateName: [
          {
            required: true,
            message: "不能为空",
            trigger: "blur",
          },
        ],
      },
      formItem: {
        reportRowId: "",
        reportTitle: "",
        levelRow: "",
        levelRowName: "",
        templateId: "",
        templateName: "",
        parentId: "",
        reportResult: "",
        reportConclusion: "",
        orderNum: 0,
      },

      data: [],
      // 显示搜索条件
      showSearch: true,
      // 非多个禁用
      multiple: true,
      ids: "",
      single: "",
      open: false,
      rowOptions: [],
    };
  },
  methods: {
    getList() {
      this.loading = true;
      treeList(this.addDateRange(this.queryParams, this.dateRange)).then(
        (response) => {
          this.data = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleModalAdd() {
      this.getTreeselect();
      this.handleSearchTemplate();
      this.handleReset();
      this.modalTitle = "添加报告行信息";
      this.open = true;
    },
    handleResetForm(form) {
      this.$refs[form].resetFields();
    },
    handleSearchTemplate() {
      getAllTemplate().then((response) => {
        this.templates = response.data;
      });
    },
    handleReset() {
      const newData = {
        reportRowId: undefined,
        reportTitle: undefined,
        levelRow: undefined,
        levelRowName: undefined,
        templateId: undefined,
        templateName: undefined,
        parentId: 0,
        reportResult: undefined,
        reportConclusion: undefined,
        orderNum: 0,
      };
      this.isEdit = false;
      this.formItem = newData;
      this.open = false;
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.reportRowId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },

    changeLavel(changeValue) {
      var levelRowNameArr = changeValue.split("_");
      this.formItem.levelRow = levelRowNameArr[0];
      this.formItem.levelRowName = levelRowNameArr[1];
    },
    changeTemplate(changeValue) {
      var templateNameArr = changeValue.split("_");
      this.formItem.templateId = templateNameArr[0];
      this.formItem.templateName = templateNameArr[1];
    },

    submitForm() {
      var msg = "";
      this.$refs["dialogForm"].validate((valid) => {
        if (valid) {
          msg =
            this.formItem.reportRowId == undefined ? "添加成功" : "修改成功";
          saveReportRow(this.formItem).then((response) => {
            this.msgSuccess(msg);
            this.open = false;
            this.getList();
          });
        }
      });
    },
    handleModalUpdate(data) {
      this.handleReset();
      this.getTreeselect();
      this.handleSearchTemplate();
      this.getInfoByRowId(data.reportRowId);
      this.modalTitle = "编报告行信息";
      this.open = true;
    },
    getInfoByRowId(rowId) {
      getInfoByRowId(rowId).then((response) => {
        if (response.code === 200) {
          this.formItem = response.data;
        }
      });
    },
    handleRemove(row) {
      const rowIds = row.reportRowId || this.ids;
      this.$confirm("确认删除吗?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return delReportRow(rowIds);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(() => {
          this.getList();
          this.msgError("您删除的选择含有子标题，请删除子标题后重试");
        });
    },
    /** 查询菜单下拉树结构 */
    getTreeselect() {
      listRow().then((response) => {
        this.rowOptions = [];
        const row = { reportRowId: 0, reportTitle: "无", children: [] };
        row.children = this.handleTree(response.data, "reportRowId");
        this.rowOptions.push(row);
      });
    },
    /** 转换菜单数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.reportRowId,
        label: node.reportTitle,
        children: node.children,
      };
    },
  },
  mounted: function () {
    this.getList();
  },
};
</script>
<style>
.searchItem {
  width: 300px;
  float: left;
}

.handleBtn {
  margin-left: 15px;
}
</style>
