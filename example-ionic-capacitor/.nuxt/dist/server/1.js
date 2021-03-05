exports.ids = [1];
exports.modules = {

/***/ 18:
/***/ (function(module, exports, __webpack_require__) {

// style-loader: Adds some css to the DOM by adding a <style> tag

// load the styles
var content = __webpack_require__(20);
if(typeof content === 'string') content = [[module.i, content, '']];
if(content.locals) module.exports = content.locals;
// add CSS to SSR context
var add = __webpack_require__(7).default
module.exports.__inject__ = function (context) {
  add("bd1bf048", content, true, context)
};

/***/ }),

/***/ 19:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _vue_style_loader_index_js_ref_3_oneOf_1_0_GitHub_cordova_sdk_dev_example_ionic_capacitor_node_modules_css_loader_dist_cjs_js_ref_3_oneOf_1_1_vue_loader_lib_loaders_stylePostLoader_js_GitHub_cordova_sdk_dev_example_ionic_capacitor_node_modules_postcss_loader_dist_cjs_js_ref_3_oneOf_1_2_vue_loader_lib_index_js_vue_loader_options_index_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(18);
/* harmony import */ var _vue_style_loader_index_js_ref_3_oneOf_1_0_GitHub_cordova_sdk_dev_example_ionic_capacitor_node_modules_css_loader_dist_cjs_js_ref_3_oneOf_1_1_vue_loader_lib_loaders_stylePostLoader_js_GitHub_cordova_sdk_dev_example_ionic_capacitor_node_modules_postcss_loader_dist_cjs_js_ref_3_oneOf_1_2_vue_loader_lib_index_js_vue_loader_options_index_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(_vue_style_loader_index_js_ref_3_oneOf_1_0_GitHub_cordova_sdk_dev_example_ionic_capacitor_node_modules_css_loader_dist_cjs_js_ref_3_oneOf_1_1_vue_loader_lib_loaders_stylePostLoader_js_GitHub_cordova_sdk_dev_example_ionic_capacitor_node_modules_postcss_loader_dist_cjs_js_ref_3_oneOf_1_2_vue_loader_lib_index_js_vue_loader_options_index_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0__);
/* harmony reexport (unknown) */ for(var __WEBPACK_IMPORT_KEY__ in _vue_style_loader_index_js_ref_3_oneOf_1_0_GitHub_cordova_sdk_dev_example_ionic_capacitor_node_modules_css_loader_dist_cjs_js_ref_3_oneOf_1_1_vue_loader_lib_loaders_stylePostLoader_js_GitHub_cordova_sdk_dev_example_ionic_capacitor_node_modules_postcss_loader_dist_cjs_js_ref_3_oneOf_1_2_vue_loader_lib_index_js_vue_loader_options_index_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0__) if(["default"].indexOf(__WEBPACK_IMPORT_KEY__) < 0) (function(key) { __webpack_require__.d(__webpack_exports__, key, function() { return _vue_style_loader_index_js_ref_3_oneOf_1_0_GitHub_cordova_sdk_dev_example_ionic_capacitor_node_modules_css_loader_dist_cjs_js_ref_3_oneOf_1_1_vue_loader_lib_loaders_stylePostLoader_js_GitHub_cordova_sdk_dev_example_ionic_capacitor_node_modules_postcss_loader_dist_cjs_js_ref_3_oneOf_1_2_vue_loader_lib_index_js_vue_loader_options_index_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0__[key]; }) }(__WEBPACK_IMPORT_KEY__));


/***/ }),

/***/ 20:
/***/ (function(module, exports, __webpack_require__) {

// Imports
var ___CSS_LOADER_API_IMPORT___ = __webpack_require__(6);
var ___CSS_LOADER_EXPORT___ = ___CSS_LOADER_API_IMPORT___(function(i){return i[1]});
// Module
___CSS_LOADER_EXPORT___.push([module.i, ".Landscape{min-height:calc(100vh - 200px);background-color:#fff;padding:80px 15px 70px;text-align:center}@media (min-width:992px){.Landscape{padding:140px 30px 0}}.Landscape__Logo__Title{margin:0;font-family:\"Quicksand\",\"Source Sans Pro\",-apple-system,BlinkMacSystemFont,\"Segoe UI\",Roboto,\"Helvetica Neue\",Arial,sans-serif;display:block;font-weight:300;font-size:100px;color:#35495e;letter-spacing:1px;padding:0 15px}@media (min-width:992px){.Landscape__Logo__Title{font-size:120px;display:inline-block;padding:0 0 0 30px}}.Landscape__Title{font-size:45px;font-weight:300;line-height:normal;margin:10px 0 0;color:#526488;word-spacing:5px}.Landscape__Page__Explanation,.Landscape__Title{font-family:\"Quicksand\",\"Source Sans Pro\",-apple-system,BlinkMacSystemFont,\"Segoe UI\",Roboto,\"Helvetica Neue\",Arial,sans-serif}.Landscape__Page__Explanation{color:#35495e;margin:16px 0 0;font-size:16px}.Landscape__Page__Explanation>a{color:#3b8070;text-decoration:underline}@media (min-width:992px){.Landscape__Title{font-size:60px}}.button{font-family:\"Quicksand\",\"Source Sans Pro\",-apple-system,BlinkMacSystemFont,\"Segoe UI\",Roboto,\"Helvetica Neue\",Arial,sans-serif;position:relative;display:inline-block;color:#fff!important;font-size:16px;font-weight:600;padding:13px 42px 14px;min-width:150px;text-align:center;text-transform:uppercase;text-decoration:none;background-color:#3b8070;border-radius:4px;letter-spacing:1px;border:1px solid #3b8070;margin-top:40px}", ""]);
// Exports
module.exports = ___CSS_LOADER_EXPORT___;


/***/ }),

/***/ 21:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
// ESM COMPAT FLAG
__webpack_require__.r(__webpack_exports__);

// CONCATENATED MODULE: /Users/uerceg/.npm/_npx/34409/lib/node_modules/nuxt/node_modules/vue-loader/lib/loaders/templateLoader.js??vue-loader-options!/Users/uerceg/.npm/_npx/34409/lib/node_modules/nuxt/node_modules/vue-loader/lib??vue-loader-options!/Users/uerceg/.npm/_npx/34409/lib/node_modules/nuxt/node_modules/@nuxt/vue-app/template/pages/index.vue?vue&type=template&id=fe99f918&
var render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('div',[_vm._ssrNode("<section class=\"Landscape\"><div class=\"Landscape__Logo\"><svg width=\"220\" height=\"166\" xmlns=\"http://www.w3.org/2000/svg\" class=\"logo\"><g transform=\"translate(-18 -29)\" fill=\"none\" fill-rule=\"evenodd\"><path d=\"M0 176h67.883a22.32 22.32 0 0 1 2.453-7.296L134 57.718 100.881 0 0 176zM218.694 176H250L167.823 32 153 58.152l62.967 110.579a21.559 21.559 0 0 1 2.727 7.269z\"></path> <path d=\"M86.066 189.388a8.241 8.241 0 0 1-.443-.908 11.638 11.638 0 0 1-.792-6.566H31.976l78.55-138.174 33.05 58.932L154 94.882l-32.69-58.64C120.683 35.1 116.886 29 110.34 29c-2.959 0-7.198 1.28-10.646 7.335L20.12 176.185c-.676 1.211-3.96 7.568-.7 13.203C20.912 191.95 24.08 195 31.068 195h66.646c-6.942 0-10.156-3.004-11.647-5.612z\" fill=\"#00C58E\"></path> <path d=\"M235.702 175.491L172.321 62.216c-.655-1.191-4.313-7.216-10.68-7.216-2.868 0-6.977 1.237-10.32 7.193L143 75.706v26.104l18.709-32.31 62.704 111.626h-23.845c.305 1.846.134 3.74-.496 5.498a7.06 7.06 0 0 1-.497 1.122l-.203.413c-3.207 5.543-10.139 5.841-11.494 5.841h37.302c1.378 0 8.287-.298 11.493-5.841 1.423-2.52 2.439-6.758-.97-12.668z\" fill=\"#108775\"></path> <path d=\"M201.608 189.07l.21-.418c.206-.364.378-.745.515-1.139a10.94 10.94 0 0 0 .515-5.58 16.938 16.938 0 0 0-2.152-5.72l-49.733-87.006L143.5 76h-.136l-7.552 13.207-49.71 87.006a17.534 17.534 0 0 0-1.917 5.72c-.4 2.21-.148 4.486.725 6.557.13.31.278.613.444.906 1.497 2.558 4.677 5.604 11.691 5.604h92.592c1.473 0 8.651-.302 11.971-5.93zm-58.244-86.657l45.455 79.52H97.934l45.43-79.52z\" fill=\"#2F4A5F\" fill-rule=\"nonzero\"></path></g></svg></div> <h2 class=\"Landscape__Title\">The Vue.js Framework</h2> <a href=\"https://nuxtjs.org/guide/installation#starting-from-scratch\" target=\"_blank\" class=\"button\">\n      Get Started\n    </a> <p class=\"Landscape__Page__Explanation\">Please create <a href=\"https://nuxtjs.org/guide/directory-structure#the-pages-directory\" target=\"_blank\">the pages directory</a> to suppress this default page.</p></section>")])}
var staticRenderFns = []


// CONCATENATED MODULE: /Users/uerceg/.npm/_npx/34409/lib/node_modules/nuxt/node_modules/@nuxt/vue-app/template/pages/index.vue?vue&type=template&id=fe99f918&

// EXTERNAL MODULE: /Users/uerceg/.npm/_npx/34409/lib/node_modules/nuxt/node_modules/vue-loader/lib/runtime/componentNormalizer.js
var componentNormalizer = __webpack_require__(2);

// CONCATENATED MODULE: /Users/uerceg/.npm/_npx/34409/lib/node_modules/nuxt/node_modules/@nuxt/vue-app/template/pages/index.vue

var script = {}
function injectStyles (context) {
  
  var style0 = __webpack_require__(19)
if (style0.__inject__) style0.__inject__(context)

}

/* normalize component */

var component = Object(componentNormalizer["a" /* default */])(
  script,
  render,
  staticRenderFns,
  false,
  injectStyles,
  null,
  "1a951162"
  
)

/* harmony default export */ var pages = __webpack_exports__["default"] = (component.exports);

/***/ })

};;
//# sourceMappingURL=1.js.map