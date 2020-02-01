import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div code-based-recipe-base=\"\">
    +             <div className=\"top-level-tabs objecttype-recipe\">
    +                 <div className=\"horizontal-flex row-fluid object-nav\">
    +                     <div className=\"flex oh\" std-object-breadcrumb=\"\">
    +                         <div className=\"noflex\">
    +                             <a
    +                                 className=\"{enabled: topNav.tab == 'summary'}\"
    +                                 onClick={() => {
    +                                     topNav.tab = 'summary';
    +                                 }}
    +                                 fw500-width=\"\">
    +                                 概要
    +                             </a>
    +                             <span data-block=\"middleTabs\">
    +                                 <a
    +                                     className=\"{enabled: topNav.tab == 'code'}\"
    +                                     onClick={() => {
    +                                         topNav.tab = 'code';
    +                                     }}
    +                                     fw500-width=\"\">
    +                                     代码
    +                                 </a>
    +                             </span>
    +                             <a
    +                                 className=\"{enabled: topNav.tab == 'io'}\"
    +                                 onClick={() => {
    +                                     topNav.tab = 'io';
    +                                 }}
    +                                 fw500-width=\"\">
    +                                 输入/输出
    +                             </a>
    +                             <a
    +                                 className=\"{enabled: topNav.tab == 'advanced'}\"
    +                                 onClick={() => {
    +                                     topNav.tab = 'advanced';
    +                                 }}>
    +                                 高级
    +                             </a>
    +                             <a
    +                                 className=\"{enabled: topNav.tab == 'gitlog'}\"
    +                                 onClick={() => {
    +                                     topNav.tab = 'gitlog';
    +                                 }}
    +                                 fw500-width=\"\">
    +                                 历史
    +                             </a>
    + 
    +                             <div className=\"otherLinks\">
    +                                 <div discussions-button=\"\">
    +                                     <div
    +                                         style=\"display: inline-block\"
    +                                         include-no-scope=\"/templates/recipes/fragments/recipe-save-button.html\">
    +                                         <div
    +                                             style=\"display: inline-block\"
    +                                             include-no-scope=\"/templates/recipes/fragments/recipe-tabs-other-links.html\"
    +                                         />
    +                                     </div>
    +                                 </div>
    +                             </div>
    + 
    +                             <div include-no-scope=\"/templates/recipes/fragments/recipe-summary-tab.html\">
    +                                 <div include-no-scope=\"/templates/recipes/fragments/recipe-io-tab.html\">
    +                                     <div include-no-scope=\"/templates/recipes/fragments/recipe-git-log.html\">
    +                                         <div data-block=\"middleTabsContent\">
    +                                             {topNav.tab == 'code' ? (
    +                                                 <div
    +                                                     className=\"{showLeftPane: showLeftPane, showRightPane: showRightPane}\"
    +                                                     fixed-panes=\"\"
    +                                                     show-left-pane=\"true\"
    +                                                     code-based-recipe-autofill=\"\">
    +                                                     <div className=\"leftPane  recipe-right-menu\">
    +                                                         <div
    +                                                             className=\"handle\"
    +                                                             onClick={() => {
    +                                                                 toggleLeftPane();
    +                                                             }}
    +                                                         />
    +                                                         <div data-block=\"recipeLeftTabs\">
    +                                                             <tabs>
    +                                                                 <pane title=\"数据集\" icon=\"dataset\">
    +                                                                     <div include-no-scope=\"/templates/recipes/fragments/code-based-recipe-right-datasets-tab.html\">
    +                                                                         <pane title=\"变量\" icon=\"list\">
    +                                                                             <div include-no-scope=\"/templates/recipes/fragments/code-based-recipe-right-variables-tab.html\" />
    +                                                                         </pane>
    +                                                                     </div>
    +                                                                     <div
    +                                                                         className=\"code-based-recipe-main-zone\"
    +                                                                         data-block=\"recipeEditor\"
    +                                                                     />
    +                                                                 </pane>
    +                                                             </tabs>
    +                                                         </div>
    +                                                     </div>
    + 
    +                                                     <div data-block=\"additionalTabsContent\" />
    +                                                 </div>
    +                                             ) : null}
    +                                         </div>
    +                                     </div>
    +                                 </div>
    +                             </div>
    +                         </div>
    +                     </div>
    +                 </div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;