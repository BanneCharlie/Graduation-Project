<template>
    <el-card class="box-card">
      <el-table size="medium" style="width: 100%;margin-top: 20px;" :data="processList" v-loading="loading">
        <el-table-column type="index" label="序号" align="center" width="100"></el-table-column>
        <el-table-column prop="sxpzName" label="事项配置名称" align="left" width="1080"></el-table-column>
        <el-table-column label="操作" align="center" width="100">
          <template slot-scope="scope">
              <el-button class="handleBtn" type="primary" size="small" @click="handleClick(scope.row)">确定</el-button>
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
            processKey:'',
            businessKey:'',
            templateType:'',
            sxpzId:'',
            swId:'',//
            type:'',//
            dwOrgId:'',
            flag:'',//
            fileRelationBeanId:'',//
            swtofw:'',//
            swtaskId:''//
          }
        }
      },
      methods: {
        getList() {
          this.loading = true;
          getProcessList("key-pro-report-manage").then(response => {
              this.processList = response.data.sxpzList;
              this.processInfo.fileRelationBeanId = response.data.fileRelationBeanId;
              this.processInfo.flag = response.data.flag;
              this.processInfo.swId = response.data.swId;
              this.processInfo.swtaskId = response.data.swtaskId;
              this.processInfo.swtofw = response.data.swtofw;
              this.processInfo.type = response.data.type;
              this.loading = false;
            }
          );
        },
        handleClick(row) {
          goProcessTemplate({
            fileRelationBeanId:  this.processInfo.fileRelationBeanId,
            flag:  this.processInfo.flag,
            swId:  this.processInfo.swId,
            swtaskId:  this.processInfo.swtaskId,
            swtofw:  this.processInfo.swtofw,
            type:  this.processInfo.type,
            processKey:  row.ptId,
            businessKey:  row.sxId,
            sxpzId:  row.sxpzRowId,
            dwOrgId:  row.deptId,
            templateType:  'start', 
            processInstId: ''
          }).then(response => {
            this.$router.push({name:'templatePage', params:{processUrlVoJson: JSON.stringify(response.data)}});
            
            /* this.$router.push({name:'templatePage', params:{
              formUrl: response.formUrl,
              fileUrl: response.fileUrl,
              fileRelationUrl: response.fileRelationUrl,
              adviceUrl: response.adviceUrl,
              processUrl: response.processUrl,
              operateUrl: response.operateUrl,
              redirectUrl: response.redirectUrl,
              templateType: response.templateType,
              sendReadUrl: response.sendReadUrl,
              wordUrl: response.wordUrl,
              businessKey: row.businessKey,
              }}); */
              
          });
        }
      },
      mounted: function() {
        this.getList()
      }
    }
  </script>
  
  <style>
  </style>
  