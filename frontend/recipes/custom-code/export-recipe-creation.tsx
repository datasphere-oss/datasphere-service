import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div
    +             ng-controller=\"ExportRecipeCreationController\"
    +             className=\"modal modal3 code-based-recipe-creation-modal io-modal\"
    +             auto-size=\"false\">
    +             <div className=\"vertical-flex h100\">
    +                 <div
    +                     dku-modal-header-with-totem=\"\"
    +                     modal-class=\"noflex\"
    +                     modal-title=\"新建导出组件\"
    +                     modal-totem=\"icon-visual_export_recipe\">
    +                     <div className=\"flex\">
    +                         <form className=\"fh dkuform-modal-wrapper\" name=\"newRecipeForm\">
    +                             <div className=\"modal-body h100\">
    +                                 <div block-api-error=\"\" />
    +                                 {computablesMap ? (
    +                                     <div className=\"row-fluid recipe-modal-io h100\">
    +                                         <div className=\"half-pane\">
    +                                             <div
    +                                                 recipe-io-inputs=\"\"
    +                                                 roles=\"recipeDesc.inputRoles\"
    +                                                 location=\"modal\"
    +                                                 className=\"h100\"
    +                                             />
    +                                             <div className=\"half-pane\">
    +                                                 <div
    +                                                     recipe-io-outputs=\"\"
    +                                                     roles=\"recipeDesc.outputRoles\"
    +                                                     location=\"modal\"
    +                                                     className=\"h100\"
    +                                                 />
    +                                             </div>
    +                                         </div>
    +                                     </div>
    +                                 ) : null}
    + 
    +                                 <div className=\"noflex modal-footer modal-footer-std-buttons\">
    +                                     <button
    +                                         type=\"button\"
    +                                         className=\"btn btn-default\"
    +                                         onClick={() => {
    +                                             dismiss();
    +                                         }}>
    +                                         取消
    +                                     </button>
    +                                     <button
    +                                         type=\"submit\"
    +                                         disabled={newRecipeForm.$invalid || creatingRecipe}
    +                                         className=\"btn btn-primary\"
    +                                         onClick={() => {
    +                                             createRecipe();
    +                                         }}>
    +                                         新建处理组件
    +                                     </button>
    +                                 </div>
    +                             </div>
    +                         </form>
    +                     </div>
    +                 </div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;