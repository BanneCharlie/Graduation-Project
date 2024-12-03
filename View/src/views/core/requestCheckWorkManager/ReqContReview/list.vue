<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      v-show="showSearch"
      :inline="true"
      size="small"
      label-suffix=":"
    >
      <el-form-item label="申请部门" prop="createDeptName">
        <el-input
          v-model="queryParams.createDeptName"
          placeholder="请输入申请部门"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="申请时间">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd HH:mm:ss"
          :default-time="['00:00:00', '23:59:59']"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
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
    <el-col :span="1.5">
      <el-button
        type="danger"
        size="mini"
        icon="el-icon-delete"
        :disabled="multiple"
        @click="handleDelete"
        >删除</el-button
      >
    </el-col>
    <right-toolbar
      :showSearch.sync="showSearch"
      @queryTable="getList"
    ></right-toolbar>

    <el-table
      v-loading="loading"
      size="small"
      :data="reqContReviewList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column
        label="合同编号"
        align="center"
        prop="contractNumber"
        width="180"
      />
      <el-table-column
        label="申请人"
        align="center"
        prop="createUserName"
        width="140"
      />
      <el-table-column
        label="申请部门"
        align="center"
        prop="createDeptName"
        width="150"
      />
      <el-table-column
        label="委托单位"
        align="center"
        prop="entrustUnit"
        width="150"
      />
      <el-table-column
        label="使用单位"
        align="center"
        prop="useUnit"
        width="150"
      />
      <el-table-column
        label="申请时间"
        align="center"
        prop="createTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseUTCTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-circle-check"
            @click="handleDataView(scope.row)"
            >查看</el-button
          >
          <!-- v-if="user.deptId == '10113'" -->
          <el-button
            v-if="user.deptId == '10113'"
            type="success"
            size="mini"
            icon="el-icon-s-check"
            @click="handlePayment(scope.row, 'request')"
            >缴费信息</el-button
          >
          <el-button
            v-if="user.deptId != '10113'"
            type="danger"
            size="mini"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
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
  </div>
</template>

<script>
import {
  listReqContReview,
  delReqContReview,
} from "@/api/business/reqContReview";
import { goProcessTemplate } from "@/api/process/process";
import { getCurrentUserInfo } from "@/api/system/user";

export default {
  name: "Role",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 委托合同评审数据列表
      reqContReviewList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        createDeptName: undefined,
        createTime: undefined,
      },
      user: {},
      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "label",
      },
      //要删除人员的名称
      names: [],
      sxpzInfo: {},
      processInfo: {},
      //流程是否已经结束
      isEnd: false,
    };
  },
  created() {
    this.getList();
  },
  mounted() {
    this.getCreateUserInfo();
  },
  methods: {
    /** 查询委托合同评审列表 */
    getList() {
      this.loading = true;
      listReqContReview(
        this.addDateRange(this.queryParams, this.dateRange)
      ).then((response) => {
        this.reqContReviewList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.rowId);
      this.names = selection.map((item) => item.createUserName);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const rowIds = row.rowId || this.ids;
      const usernams = row.createUserName || this.names;
      this.$confirm('是否确认删除"' + usernams + '"的申请?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return delReqContReview(rowIds);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        });
    },
    parseUTCTime(time) {
      //2021-05-31T14:23:22.000+0800
      return time.replaceAll("T", " ").split(".")[0];
    },
    handleDataView(row) {
      goProcessTemplate({
        fileRelationBeanId: "",
        flag: "",
        swId: "",
        swtaskId: "",
        swtofw: "",
        type: "",
        processKey: "",
        businessKey: row.businessKey,
        sxpzId: "",
        dwOrgId: "",
        templateType: "view",
        processInstId: row.piid,
      }).then((response) => {
        this.$router.push({
          name: "templatePageView",
          params: { processUrlVoJson: JSON.stringify(response.data) },
        });
      });
    },
    getCreateUserInfo() {
      getCurrentUserInfo().then((response) => {
        this.user = response.data;
      });
    },
    // 缴费处理
    handlePayment(row, businessCategory) {
      console.log(row);
      console.log(row.contractMoney);
      this.$router.push({
        path: "/checkwork/paymentConfirm",
        query: {
          data: row,
          businessCategory: businessCategory,
        },
      });
    },
  },
};
</script>

<style>
</style>
