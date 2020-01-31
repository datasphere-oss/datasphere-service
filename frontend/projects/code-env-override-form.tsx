import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <form className=\"dkuform-horizontal\">
    +             <div className=\"control-group\">
    +                 <label htmlFor=\"\" className=\"control-label\">
    +                     使用 DSS 内置 {capitalize(envLang.toLowerCase())} 环境
    +                 </label>
    +                 <div className=\"controls\">
    +                     <input type=\"checkbox\" value={envSelection.useBuiltinEnv} />
    +                     <span className=\"help-inline\">此项目中的默认代码环境是DSS的内置环境</span>
    +                 </div>
    +             </div>
    +             {!envSelection.useBuiltinEnv ? (
    +                 <div className=\"control-group\">
    +                     <label htmlFor=\"\" className=\"control-label\">
    +                         环境
    +                     </label>
    +                     <div className=\"controls\">
    +                         <select
    +                             dku-bs-select=\"\"
    +                             value={envSelection.envName}
    +                             ng-options=\"env.envName as env.envName for env in envNamesWithDescs.envs\"
    +                             watch-model=\"envNamesWithDescs\"
    +                         />
    +                     </div>
    +                 </div>
    +             ) : null}
    +             {envSelection.useBuiltinEnv || envSelection.envName.length > 0 ? (
    +                 <div className=\"control-group\">
    +                     <label htmlFor=\"\" className=\"control-label\">
    +                         防止组件覆盖
    +                     </label>
    +                     <div className=\"controls\">
    +                         <input type=\"checkbox\" value={envSelection.preventOverride} />
    +                         <span className=\"help-inline\">此项目中的组件被强制使用此处定义的代码环境，并且不能覆盖</span>
    +                     </div>
    +                 </div>
    +             ) : null}
    +         </form>
    +     );
    + };
    + 
    + export default TestComponent;