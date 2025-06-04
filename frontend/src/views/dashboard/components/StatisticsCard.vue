<template>
  <el-card class="statistics-card" :body-style="{ padding: '20px' }">
    <div class="card-content">
      <div class="icon-wrapper" :style="{ backgroundColor: color }">
        <el-icon :size="24" color="#fff">
          <component :is="icon" />
        </el-icon>
      </div>
      <div class="info-wrapper">
        <div class="title">{{ title }}</div>
        <div class="value">{{ value }}</div>
      </div>
    </div>
  </el-card>
</template>

<script setup>
defineProps({
  title: {
    type: String,
    required: true
  },
  value: {
    type: String,
    required: true
  },
  icon: {
    type: String,
    required: true
  },
  color: {
    type: String,
    default: '#409EFF'
  }
})
</script>

<style lang="scss" scoped>
.statistics-card {
  .card-content {
    display: flex;
    align-items: center;

    .icon-wrapper {
      width: 80px;
      height: 80px;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 20px;
    }

    .info-wrapper {
      flex: 1;

      .title {
        font-size: 14px;
        color: #909399;
        margin-bottom: 8px;
      }

      .value {
        font-size: 24px;
        font-weight: bold;
        color: #303133;
      }
    }
  }
}
</style>