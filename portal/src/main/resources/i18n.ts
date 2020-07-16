import i18n from "i18next";
import {initReactI18next} from "react-i18next";

const translationFR = require("./locales/fr/translation.json");

const resources = {
  fr: {
    translation: translationFR
  }
}

i18n
  .use(initReactI18next)
  .init({
    resources,
    fallbackLng: 'fr',
    interpolation: {
      escapeValue: true
    }
  });

export default i18n;
