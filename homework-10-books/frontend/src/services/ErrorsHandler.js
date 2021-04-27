import Notification from "element-ui/packages/notification/src/main";

const INTERNAL_ERROR = { code: 500, message: "На сервере произошла ошибка" };
const NOT_FOUND_ERROR = { code: 404, message: "Сервер временно недоступен..." };
const ACCESS_DENIED_ERROR = { code: 403, message: "Доступ запрещен..." };
const UNKNOWN_ERROR = { code: 0, message: "Неизвестная ошибка..." };

const errors = new Map();
errors.set(INTERNAL_ERROR.code, INTERNAL_ERROR);
errors.set(NOT_FOUND_ERROR.code, NOT_FOUND_ERROR);
errors.set(ACCESS_DENIED_ERROR.code, NOT_FOUND_ERROR);

const ERROR_TITLE = "Ошибка";
export default {
  getErrorByCode(statusCode, message) {
    let error = !errors.has(statusCode)
      ? UNKNOWN_ERROR
      : errors.get(statusCode);
    return `${error.message} ${message}`;
  },
  notifyError(message) {
    Notification.error({ title: ERROR_TITLE, message });
  },
};
