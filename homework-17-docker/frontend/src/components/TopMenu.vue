<template>
  <el-menu
    :default-active="activeIndex"
    mode="horizontal"
    @select="handleSelect"
    background-color="#545c64"
    text-color="#fff"
    active-text-color="#ffd04b"
  >
    <el-menu-item v-for="item in menu" :index="item.id" :key="item.id">{{
      item.name
    }}</el-menu-item>
  </el-menu>
</template>
<script>
const menuItems = [
  { id: "Books", name: "Книги" },
  { id: "Authors", name: "Авторы" },
  { id: "Genres", name: "Жанры" },
];

export default {
  name: "TopMenu",
  data() {
    return {
      activeIndex: "Books",
    };
  },
  computed: {
    menu: function () {
      return menuItems;
    },
  },
  methods: {
    handleSelect(key) {
      this.$router.push(key);
    },
  },
};
</script>
