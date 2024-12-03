<template>
  <div>
    <!-- 提交  重置 -->
    <el-card class="box-card" shadow="never">
      <el-row>
        <el-col :span="24" style="text-align: center;">
          <el-button @click="handleReturn" type="danger" round style="width: 150px;">返回</el-button>
        </el-col>
      </el-row>
    </el-card>

  </div>
</template>

<script>
  export default {
    name: "operateUrl",
    data() {
      return {}
    },
    methods: {
      handleReturn() {
        this.$emit('operate-return')
      }
    },
    created() {},
    mounted: function() {

    },
  }
</script>

