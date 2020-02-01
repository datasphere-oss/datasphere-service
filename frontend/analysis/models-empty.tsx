import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return mlTaskStatus && !mlTaskStatus.fullModelIds.length ? (
    +         <div
    +             className=\"flex list--empty\"
    +             ng-init=\"verticaPrediction = mlTasksContext.activeMLTask.backendType === 'VERTICA' &amp;&amp; mlTasksContext.activeMLTask.taskType === 'PREDICTION'\">
    +             {!mlTaskStatus.fullModelIds.length && mlTaskJustCreated && !verticaPrediction ? (
    +                 <span>
    +                     你的模型设计已经准备好.
    +                     <div className=\"moreinfo\">
    +                         现在能够训练它 或者 <NavLink to=\"^.design\">检查此设计</NavLink>.
    +                     </div>
    +                     {mlTaskDesign.taskType === 'PREDICTION' ? (
    +                         <div className=\"moreinfo\">Target variable : {mlTaskDesign.targetVariable}</div>
    +                     ) : null}
    +                     <div className=\"moreinfo\">
    +                         {pluralize(getEnabledModels(mlTaskDesign.modeling).length, 'algorithm', 'algorithms')} -
    +                         {pluralize(
    +                             mlTaskFeatures(mlTaskDesign.preprocessing.per_feature).length,
    +                             'feature',
    +                             'features'
    +                         )}{' '}
    +                         selected
    +                     </div>
    +                     <div className=\"mtop10\">
    +                         <button
    +                             onClick={() => {
    +                                 trainDirectly();
    +                             }}
    +                             className=\"btn btn-success btn-cta-big-mod\">
    +                             <i className=\"icon-play\">&nbsp; 训练</i>
    +                         </button>
    +                         <i className=\"icon-play\" />
    +                     </div>
    +                     <i className=\"icon-play\" />
    +                 </span>
    +             ) : null}
    +             <i className=\"icon-play\">
    +                 {!mlTaskStatus.fullModelIds.length && mlTaskJustCreated && verticaPrediction ? (
    +                     <span>
    +                         你的模型设计已经准备好.
    +                         <div className=\"moreinfo\">
    +                             Vertica 需要测试集作为单独表. 请指定一个在设置中 &gt; 训练 &amp; 验证.
    +                         </div>
    +                         <div className=\"mtop10\">
    +                             <NavLink className=\"btn btn-default btn-cta-big-mod\" to={`${sRefPrefix}.settings`}>
    +                                 <i className=\"icon-cogs\">&nbsp; 设置</i>
    +                             </NavLink>
    +                             <i className=\"icon-cogs\" />
    +                         </div>
    +                         <i className=\"icon-cogs\" />
    +                     </span>
    +                 ) : null}
    +                 <i className=\"icon-cogs\">
    +                     {!mlTaskStatus.fullModelIds.length && !mlTaskJustCreated ? (
    +                         <span>
    +                             无模型训练.
    +                             {appConfig.userProfile.mayVisualML ? (
    +                                 <div className=\"moreinfo\">
    +                                     现在能够训练它 或者 <NavLink to=\"^.design\">检查此设置</NavLink>.
    +                                 </div>
    +                             ) : null}
    +                             {mlTaskDesign.taskType === 'PREDICTION' ? (
    +                                 <div className=\"moreinfo\">目标变量 : {mlTaskDesign.targetVariable}</div>
    +                             ) : null}
    +                             <div className=\"moreinfo\">
    +                                 {pluralize(getEnabledModels(mlTaskDesign.modeling).length, 'algorithm', 'algorithms')} -
    +                                 {pluralize(
    +                                     mlTaskFeatures(mlTaskDesign.preprocessing.per_feature).length,
    +                                     'feature',
    +                                     'features'
    +                                 )}{' '}
    +                                 已选
    +                             </div>
    +                             <div visual-ml-access-check=\"\" className=\"mtop10\">
    +                                 <button
    +                                     onClick={() => {
    +                                         newTrainSession();
    +                                     }}
    +                                     className=\"btn btn-success btn-cta-big-mod\">
    +                                     <i className=\"icon-play\">&nbsp; 训练</i>
    +                                 </button>
    +                                 <i className=\"icon-play\" />
    +                             </div>
    +                             <i className=\"icon-play\" />
    +                         </span>
    +                     ) : null}
    +                     <i className=\"icon-play\">
    +                         {mlTaskStatus.fullModelIds.length ? <span>无模型匹配过滤器</span> : null}
    +                     </i>
    +                 </i>
    +             </i>
    +         </div>
    +     ) : null;
    + };
    + 
    + export default TestComponent;