<template>
  <el-card class="comments" shadow="never">
    <div slot="header" class="comments__header">
      <slot name="close" />
    </div>
    <el-table
      :data="comments"
      v-if="isNotEmptyComments"
      class="comments__content"
    >
      <el-table-column prop="description" label="Комментарий"></el-table-column>
      <el-table-column fixed="right" label="Действия" width="120">
        <template slot-scope="scope">
          <el-button
            @click="edit(scope.row.id)"
            size="small"
            icon="el-icon-edit"
          ></el-button>
          <el-button
            @click="remove(scope.row.id)"
            size="small"
            icon="el-icon-delete"
          ></el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-else>
      <el-alert
        title="Нет ни одного коментария к книге"
        type="info"
        :closable="false"
      ></el-alert>
    </div>
    <comment-form :comment="comment" :book-id="bookId" @saved="save" />
  </el-card>
</template>

<script>
import ErrorsHandler from "@/services/ErrorsHandler";
import CommentsService from "@/services/CommentsService";
import CommentForm from "@/components/comments/CommentForm";
import Comment from "@/domain/Comment";

export default {
  name: "Comments",
  props: ["bookId"],
  components: { CommentForm },
  data: function () {
    return {
      comments: null,
      comment: null,
    };
  },
  computed: {
    isNotEmptyComments: function () {
      if (this.comments != null) return true;
      return false;
    },
  },
  methods: {
    async loadData() {
      try {
        this.comments = await CommentsService.findAll(this.bookId);
      } catch (errorMessage) {
        ErrorsHandler.notifyError(errorMessage);
      }
    },
    async save() {
      this.$notify({
        title: "Успешно",
        message: "Комментарий успешно сохранен",
        type: "success",
      });
      this.reload();
      this.createNewComment();
    },
    async edit(commentId) {
      try {
        this.comment = await CommentsService.findById(this.bookId, commentId);
      } catch (error) {
        ErrorsHandler.notifyError(error);
      }
    },
    async remove(commentId) {
      try {
        await CommentsService.remove(this.bookId, commentId);
        this.$notify({
          title: "Успешно",
          message: "Комментарий успешно удален",
          type: "success",
        });
        this.reload();
      } catch (error) {
        ErrorsHandler.notifyError(error);
      }
    },
    reload(){
      this.loadData();
    },
    createNewComment() {
      this.comment = new Comment(this.bookId);
    },
  },
  created() {
    this.loadData();
    this.createNewComment();
  },
};
</script>
<style scoped>
.comments {
  width: 70%;
}

.comments__header {
  display: flex;
  flex-flow: row nowrap;
  justify-content: space-between;
  align-items: baseline;
  flex-grow: 2;
}
</style>
