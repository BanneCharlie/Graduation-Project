<template>
  <div>
     <!-- 提交  重置 -->
     <el-card class="box-card" shadow="never">
         <el-row>
           <el-col :span="12">
               <el-button @click="handleSubmit" :loading="false" round type="success" style="float: right;margin-right: 150px;width: 150px;">提交</el-button>
           </el-col>
           <el-col :span="12">
               <el-button @click="handleReturn" type="danger" round style="float: left;width: 150px;">返回</el-button>
           </el-col>
       </el-row>
     </el-card>

   </div>
</template>

<script>

  export default {
    name: "operateUrl",
    data() {
      return {
      }
    },
    methods: {
      handleSubmit() {
        this.$emit('operate-click')
      },
      handleReturn() {
        this.$emit('operate-return')
      }
    },
    created() {
    },
    mounted: function() {

    },
 }
</script>

