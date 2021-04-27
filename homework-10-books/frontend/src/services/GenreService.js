import Errors from "@/services/ErrorsHandler";

/**
 * Сервис для работы с api книг
 */
const url = `/api/genres`;

export default {
  /**
   * Загрузка жанров
   * @returns {Promise<unknown>}
   */
  async findAll() {
    let response = await fetch(url);
    if (response.ok) {
      let json = await response.json();
      return Promise.resolve(json);
    } else {
      return Promise.reject(
        Errors.getErrorByCode(response.statusCode, response.text())
      );
    }
  },
};
