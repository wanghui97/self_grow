<template>
  <div class="dashboard-container">
    <div class="dashboard-text">
      个人博客管理系统
    </div>
    <div>
      欢迎大家前去关注三更草堂的B站账号：<a href="https://space.bilibili.com/663528522">https://space.bilibili.com/663528522</a>
    </div>
    <div>

    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'name'
    ])
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
