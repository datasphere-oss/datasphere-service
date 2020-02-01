import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div
    +             ng-controller=\"DownloadRecipeCreationController\"
    +             className=\"modal modal3 code-based-recipe-creation-modal io-modal\"
    +             auto-size=\"false\">
    +             <div className=\"vertical-flex h100\">
    +                 <div
    +                     dku-modal-header-with-totem=\"\"
    +                     modal-class=\"noflex\"
    +                     modal-title=\"New Download recipe\"
    +                     modal-totem=\"icon-visual_download_recipe\">
    +                     <form className=\"flex dkuform-modal-wrapper dkuform-vertical-larger\" name=\"newRecipeForm\">
    +                         <div className=\"fh\">
    +                             <div className=\"vertical-flex h100\">
    +                                 <div block-api-error=\"\" />
    +                                 <div className=\"recipe-modal-io modal-body flex\">
    +                                     <div className=\"fh\">
    +                                         <div className=\"h100\">
    +                                             <div className=\"vertical-flex h100\">
    +                                                 <div className=\"flex\">
    +                                                     {io.newOutputTypeRadio == 'select' ? (
    +                                                         <div include-no-scope=\"/templates/recipes/io/visual-recipe-creation-select-folder.html\">
    +                                                             {io.newOutputTypeRadio == 'new-odb' ? (
    +                                                                 <div include-no-scope=\"/templates/recipes/io/visual-recipe-creation-new-folder.html\" />
    +                                                             ) : null}
    +                                                             <div className=\"noflex creation-types\">
    +                                                                 <div style=\"clear: both;\" />
    +                                                                 <a
    +                                                                     onClick={() => {
    +                                                                         io.newOutputTypeRadio = 'new-odb';
    +                                                                     }}
    +                                                                     className=\"{'selected-type' : io.newOutputTypeRadio == 'new-odb'}\">
    +                                                                     新建文件夹
    +                                                                 </a>
    +                                                                 |
    +                                                                 <a
    +                                                                     onClick={() => {
    +                                                                         io.newOutputTypeRadio = 'select';
    +                                                                     }}
    +                                                                     className=\"{'selected-type' : io.newOutputTypeRadio == 'select'}\">
    +                                                                     使用现有
    +                                                                 </a>
    +                                                             </div>
    +                                                         </div>
    +                                                     ) : null}
    +                                                 </div>
    +                                             </div>
    +                                         </div>
    + 
    +                                         <div
    +                                             className=\"noflex modal-footer modal-footer-std-buttons\"
    +                                             data-block=\"buttons\">
    +                                             <button
    +                                                 type=\"button\"
    +                                                 className=\"btn btn-default\"
    +                                                 onClick={() => {
    +                                                     dismiss();
    +                                                 }}>
    +                                                 取消
    +                                             </button>
    +                                             <button
    +                                                 type=\"submit\"
    +                                                 disabled={!formIsValid() || creatingRecipe}
    +                                                 className=\"btn btn-primary\"
    +                                                 onClick={() => {
    +                                                     createRecipe(false);
    +                                                 }}>
    +                                                 创建处理组件
    +                                             </button>
    +                                         </div>
    +                                     </div>
    +                                 </div>
    +                             </div>
    +                         </div>
    +                     </form>
    +                 </div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;