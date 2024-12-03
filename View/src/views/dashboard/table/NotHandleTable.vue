<template>
    <div>
       <el-table  style="width: 100%"
       :data="tableWaitCommitData"
       v-loading="tableLoading">
  
       <el-table-column type="index" label="序号" align="center" width="100"></el-table-column>
       <el-table-column prop="title" label="标题" align="center" width="500"></el-table-column>
       <el-table-column prop="createTime" label="时间" align="center"  width="200"></el-table-column>
       <el-table-column prop="currentTache"  label="当前环节" align="center" width="200"></el-table-column>
       <el-table-column prop="commitPerson" label="提交人" align="center" width="180"></el-table-column>
     </el-table>
    </div>
   </template>
   
   <script>
       export default {
  
         data() {  
           return {
            tableLoading:true, 
            tableWaitCommitData: [],
  
            }
         },
  
         methods: {
            // 从后台获取 待办事宜 和 已办事宜
          getWaitingAndCommit(){
            this.tableWaitCommitData = 
            [
              {
                  title: '南京市特检院为鼓楼区市场监督局提供电梯大数据分析服务!',
                  createTime: '2021-04-15',
                  currentTache: '办公室',
                  commitPerson: 'BBBBBBB'
              },
              {
                  title: '南京市特检院为鼓楼区市场监督局提供电梯大数据分析服务!',
                  createTime: '2021-04-15',
                  currentTache: '办公室',
                  commitPerson: 'BBBB'
              },
              {
                  title: '南京市特检院为鼓楼区市场监督局提供电梯大数据分析服务!',
                  createTime: '2021-04-15',
                  currentTache: '办公室',
                  commitPerson: 'CCCCCCC'
              }
          ];
          },
          loadingHomeData(){
            this.$message({
              message: '数据加载中...',
              type: 'warning',
              center:true,
              duration:2100,
            });
            setTimeout(() => {
              this.getWaitingAndCommit();
              this.$message({
                message: '数据加载完成',
                type: 'success',
                center:true,
                duration:2500,
            });
              this.tableLoading = false;
            }, 3000);
          }
      
        },
  
      mounted(){
        this.loadingHomeData();
      }
    }
     </script>
   
   <style>
     .el-table .warning-row {
       background: oldlace;
     }
   
     .el-table .success-row {
       background: #f0f9eb;
     }
   </style>
   