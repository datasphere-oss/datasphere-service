import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div
    +             ng-controller=\"CustomCodeRecipeCreationController\"
    +             className=\"modal modal3 code-based-recipe-creation-modal io-modal \"
    +             auto-size=\"false\">
    +             <div className=\"vertical-flex h100\">
    +                 <div
    +                     dku-modal-header-with-totem=\"\"
    +                     modal-class=\"noflex\"
    +                     modal-title={`Custom recipe \"${recipeDesc.meta.label || pluginDesc.label || pluginDesc.id}\"`}
    +                     modal-totem={recipeDesc.meta.icon || pluginDesc.icon}>
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
    +                                         Cancel
    +                                     </button>
    +                                     <button
    +                                         type=\"submit\"
    +                                         disabled={newRecipeForm.$invalid || creatingRecipe}
    +                                         className=\"btn btn-primary\"
    +                                         onClick={() => {
    +                                             createRecipe();
    +                                         }}>
    +                                         Create
    +                                     </button>
    +                                 </div>
    + 
    +                                 <div
    +                                     className=\"{'hovered' : helpState.hover}\"
    +                                     onMouseEnter={() => {
    +                                         helpState.hover = true;
    +                                     }}
    +                                     onMouseLeave={() => {
    +                                         helpState.hover = false;
    +                                     }}>
    +                                     <div className=\"hidden-help\">
    +                                         <div className=\"helpy\">
    +                                             <div className=\"help-zone oa\">
    +                                                 <div>
    +                                                     {loadedDesc.desc.meta.icon ? (
    +                                                         <i className={loadedDesc.desc.meta.icon} />
    +                                                     ) : null}
    +                                                     {!loadedDesc.desc.meta.icon && pluginDesc.icon ? (
    +                                                         <i className={pluginDesc.icon} />
    +                                                     ) : null}
    +                                                     {loadedDesc.desc.meta.label || loadedDesc.id}
    +                                                 </div>
    +                                                 <div>{loadedDesc.desc.meta.description}</div>
    +                                             </div>
    +                                         </div>
    +                                     </div>
    +                                     <div className=\"linky\">
    +                                         <a href={pluginDesc.url} title={pluginDesc.description}>
    +                                             <i className=\"icon-resize-full\" />&nbsp;Learn more
    +                                         </a>
    +                                     </div>
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