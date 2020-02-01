import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div visual-recipe=\"\" ng-controller=\"DistinctRecipeController\">
    +             {/* Load behaviors */}
    +             {/* The read-params-behavior is split from the read-params UI directive because we want the
    +     behavior to always execute, not only when we go to advanced tab */}
    +             {recipeStatus.selectedEngine.type == 'SPARK' ? (
    +                 <div>
    +                     <div spark-datasets-read-params-behavior=\"\" read-params=\"params.engineParams.sparkSQL.readParams\" />
    + 
    +                     <div className=\"top-level-tabs objecttype-recipe\">
    +                         <div include-no-scope=\"/templates/recipes/fragments/simple-recipe-top-tabs.html\" />
    + 
    +                         {/* Generic tabs */}
    +                         <div include-no-scope=\"/templates/recipes/fragments/recipe-summary-tab.html\">
    +                             <div include-no-scope=\"/templates/recipes/fragments/recipe-io-tab.html\">
    +                                 <div include-no-scope=\"/templates/recipes/fragments/recipe-git-log.html\">
    +                                     <div include-no-scope=\"/templates/recipes/visual-recipes-fragments/visual-sql-advanced-tab.html\">
    +                                         <div include-no-scope=\"/templates/recipes/distinct-recipe-main.html\" />
    +                                     </div>
    +                                 </div>
    +                             </div>
    +                         </div>
    +                     </div>
    +                 </div>
    +             ) : null}
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;