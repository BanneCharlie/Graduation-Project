<template>
  <el-card class="box-card">
    <el-table
      size="small"
      style="width: 100%; margin-top: 20px"
      :data="processList"
      v-loading="loading"
    >
      <el-table-column
        type="index"
        label="序号"
        align="center"
        width="100"
      ></el-table-column>
      <el-table-column
        prop="sxpzName"
        label="事项配置名称"
        align="center"
      ></el-table-column>
      <el-table-column label="操作" align="center" width="180">
        <template slot-scope="scope">
          <el-button
            class="handleBtn"
            type="primary"
            size="small"
            @click="handleClick(scope.row)"
            >确定</el-button
          >
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script>
import { getProcessList, goProcessTemplate } from "@/api/process/process";

export default {
  name: "processList",
  data() {
    return {
      loading: true,
      processList: [],
      processInfo: {
        processKey: "",
        businessKey: "",
        templateType: "",
        sxpzId: "",
        swId: "", //
        type: "", //
        dwOrgId: "",
        flag: "", //
        fileRelationBeanId: "", //
        swtofw: "", //
        swtaskId: "", //
      },
    };
  },
  methods: {
    getList() {
      this.loading = true;
      getProcessList("key-contract-review").then((response) => {
        this.processList = response.data.sxpzList;
        this.processInfo.fileRelationBeanId = response.data.fileRelationBeanId;
        this.processInfo.flag = response.data.flag;
        this.processInfo.swId = response.data.swId;
        this.processInfo.swtaskId = response.data.swtaskId;
        this.processInfo.swtofw = response.data.swtofw;
        this.processInfo.type = response.data.type;
        this.loading = false;
      });
    },
    handleClick(row) {
      goProcessTemplate({
        fileRelationBeanId: this.processInfo.fileRelationBeanId,
        flag: this.processInfo.flag,
        swId: this.processInfo.swId,
        swtaskId: this.processInfo.swtaskId,
        swtofw: this.processInfo.swtofw,
        type: this.processInfo.type,
        processKey: row.ptId,
        businessKey: row.sxId,
        sxpzId: row.sxpzRowId,
        dwOrgId: row.deptId,
        templateType: "start",
        processInstId: "",
      }).then((response) => {
        this.$router.push({
          name: "templatePage",
          params: { processUrlVoJson: JSON.stringify(response.data) },
        });
      });
    },
  },
  mounted: function () {
    this.getList();
  },
};
</script>

<style>
</style>
