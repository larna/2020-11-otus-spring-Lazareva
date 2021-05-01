<template>
  <div id="app">
    <el-container class="main-container">
      <el-header>
        <top-menu/>
      </el-header>
      <el-main>
        <router-view/>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import TopMenu from "./components/TopMenu.vue"

export default {
  name: "app",
  components: {
    TopMenu,
  },
};
</script>

<style>
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  max-width: 100%;
  padding: 0px;
  margin: 0px;
  display: flex;
  align-items: center;
  justify-content: center;
}
body{
  margin: 0px;
  padding: 0px;
  text-align: center;
}
.main-container{
  margin: 0px;
  max-width: 70%;
}
</style>
