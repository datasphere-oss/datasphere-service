import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return [
    +         <div key=\"child-0\" className=\"list-page-4 flex dss-home-page\">
    +             <div data-extend-template=\"/templates/layouts/list-items-2.html\" className=\"vertical-flex\">
    +                 {/* COUNTER */}
    +                 <span data-block=\"counter-name\">分析</span>
    + 
    +                 {/*  NEW   */}
    +                 <div style=\"display: inline-block\" data-block=\"newItem\">
    +                     <a
    +                         disabled-if-ro=\"\"
    +                         className=\"btn btn-default\"
    +                         onClick={() => {
    +                             newAnalysis();
    +                         }}>
    +                         <span plus-icon=\"\">&nbsp;新的分析</span>
    +                     </a>
    +                 </div>
    + 
    +                 <div data-block=\"empty\">
    +                     <h1>在这个项目中没有分析</h1>
    +                     <p>
    +                         分析是交互式可视化工作台 <br /> 交互式数据准备和机器学习.
    +                     </p>
    +                     <p className=\"small\">
    +                         从数据集的\"实验室\"上创建分析
    +                         <br />
    +                         <span guide-link=\"\" page=\"tutorials/101\">
    +                             查看教程
    +                         </span>{' '}
    +                         学习更多.
    +                     </p>
    +                 </div>
    + 
    +                 {/* ITEM LAYOUT */}
    +                 <div data-block=\"item\" className=\"h100\">
    +                     <label>
    +                         <span
    +                             style=\"display:none;\"
    +                             main-click=\"\"
    +                             onClick={(event: React.SyntheticEvent<HTMLElement>) => {
    +                                 objectClicked(item, event);
    +                             }}
    +                         />{' '}
    +                         {/* because checkbox click is prioritary */}
    +                         <input
    +                             type=\"checkbox\"
    +                             value={item.$selected}
    +                             onClick={(event: React.SyntheticEvent<HTMLElement>) => {
    +                                 checkBoxChanged(item, event);
    +                             }}
    +                         />
    +                     </label>
    +                     <div className=\"hit h100\" dataset=\"item\">
    +                         <div className=\"illustration\">
    +                             <i className=\"icon-dku-nav_analysis universe-color analysis\" />
    +                         </div>
    +                         <div className=\"hitContent horizontal-flex h100\">
    +                             <div className=\"hit-content-main noflex\">
    +                                 <h4>
    +                                     <NavLink
    +                                         to=\"projects.project.analyses.analysis.script({projectKey: $stateParams.projectKey, analysisId:item.id})\"
    +                                         title={item.name}
    +                                         dangerouslySetInnerHTML={{
    +                                             __html: 'item.name | boldify:selection.filterParams.userQueryResult'
    +                                         }}
    +                                     />
    +                                 </h4>
    +                                 <div>
    +                                     <span className=\"dib ellipsed\" style=\"max-width:500px\">
    +                                         <small>在数据集上: {item.inputDatasetSmartName}</small>
    +                                     </span>
    +                                     {item.modifiedOn ? (
    +                                         <span>
    +                                             |
    +                                             <small>修改 {friendlyTimeDeltaShort(item.modifiedOn)}</small>
    +                                         </span>
    +                                     ) : null}
    +                                 </div>
    +                             </div>
    + 
    +                             <div className=\"interests noflex\">
    +                                 {!item.interest.starred ? (
    +                                     <i
    +                                         className=\"icon-star interests-star\"
    +                                         onClick={() => {
    +                                             starObject(true, item);
    +                                         }}>
    +                                         {item.interest.starred ? (
    +                                             <i
    +                                                 className=\"icon-star interests-star active\"
    +                                                 onClick={() => {
    +                                                     starObject(false, item);
    +                                                 }}
    +                                                 title=\"You have starred this item\">
    +                                                 <br />
    +                                                 {item.interest.watching != 'YES' &&
    +                                                 item.interest.watching != 'SHALLOW' ? (
    +                                                     <i
    +                                                         className=\"icon-eye interests-watch\"
    +                                                         onClick={() => {
    +                                                             watchObject('YES', item);
    +                                                         }}>
    +                                                         {item.interest.watching == 'YES' ||
    +                                                         item.interest.watching == 'SHALLOW' ? (
    +                                                             <i
    +                                                                 className=\"icon-eye interests-watch active\"
    +                                                                 onClick={() => {
    +                                                                     watchObject('ENO', item);
    +                                                                 }}
    +                                                                 title=\"You are watching this item\"
    +                                                             />
    +                                                         ) : null}
    +                                                     </i>
    +                                                 ) : null}
    +                                             </i>
    +                                         ) : null}
    +                                     </i>
    +                                 ) : null}
    +                             </div>
    +                             {!item.interest.starred ? (
    +                                 <i
    +                                     className=\"icon-star interests-star\"
    +                                     onClick={() => {
    +                                         starObject(true, item);
    +                                     }}>
    +                                     {item.interest.starred ? (
    +                                         <i
    +                                             className=\"icon-star interests-star active\"
    +                                             onClick={() => {
    +                                                 starObject(false, item);
    +                                             }}
    +                                             title=\"You have starred this item\">
    +                                             {item.interest.watching != 'YES' && item.interest.watching != 'SHALLOW' ? (
    +                                                 <i
    +                                                     className=\"icon-eye interests-watch\"
    +                                                     onClick={() => {
    +                                                         watchObject('YES', item);
    +                                                     }}>
    +                                                     {item.interest.watching == 'YES' ||
    +                                                     item.interest.watching == 'SHALLOW' ? (
    +                                                         <i
    +                                                             className=\"icon-eye interests-watch active\"
    +                                                             onClick={() => {
    +                                                                 watchObject('ENO', item);
    +                                                             }}
    +                                                             title=\"You are watching this item\">
    +                                                             <ul className=\"tags inline flex\">
    +                                                                 {item.tags.map((tag, index: number) => {
    +                                                                     return (
    +                                                                         <li key={`item-${index}`}>
    +                                                                             <span
    +                                                                                 className=\"tag\"
    +                                                                                 color-contrast={
    +                                                                                     projectTagsMap[tag].color.substring(
    +                                                                                         1
    +                                                                                     ) || '#FDB423'
    +                                                                                 }
    +                                                                                 onClick={() => {
    +                                                                                     selectTag(
    +                                                                                         selection.filterQuery,
    +                                                                                         tag
    +                                                                                     );
    +                                                                                 }}>
    +                                                                                 {tag}
    +                                                                             </span>
    +                                                                         </li>
    +                                                                     );
    +                                                                 })}
    +                                                             </ul>
    +                                                         </i>
    +                                                     ) : null}
    +                                                 </i>
    +                                             ) : null}
    +                                         </i>
    +                                     ) : null}
    +                                 </i>
    +                             ) : null}
    +                         </div>
    +                         {!item.interest.starred ? (
    +                             <i
    +                                 className=\"icon-star interests-star\"
    +                                 onClick={() => {
    +                                     starObject(true, item);
    +                                 }}>
    +                                 {item.interest.starred ? (
    +                                     <i
    +                                         className=\"icon-star interests-star active\"
    +                                         onClick={() => {
    +                                             starObject(false, item);
    +                                         }}
    +                                         title=\"You have starred this item\">
    +                                         {item.interest.watching != 'YES' && item.interest.watching != 'SHALLOW' ? (
    +                                             <i
    +                                                 className=\"icon-eye interests-watch\"
    +                                                 onClick={() => {
    +                                                     watchObject('YES', item);
    +                                                 }}>
    +                                                 {item.interest.watching == 'YES' ||
    +                                                 item.interest.watching == 'SHALLOW' ? (
    +                                                     <i
    +                                                         className=\"icon-eye interests-watch active\"
    +                                                         onClick={() => {
    +                                                             watchObject('ENO', item);
    +                                                         }}
    +                                                         title=\"You are watching this item\"
    +                                                     />
    +                                                 ) : null}
    +                                             </i>
    +                                         ) : null}
    +                                     </i>
    +                                 ) : null}
    +                             </i>
    +                         ) : null}
    +                     </div>
    +                     {!item.interest.starred ? (
    +                         <i
    +                             className=\"icon-star interests-star\"
    +                             onClick={() => {
    +                                 starObject(true, item);
    +                             }}>
    +                             {item.interest.starred ? (
    +                                 <i
    +                                     className=\"icon-star interests-star active\"
    +                                     onClick={() => {
    +                                         starObject(false, item);
    +                                     }}
    +                                     title=\"You have starred this item\">
    +                                     {item.interest.watching != 'YES' && item.interest.watching != 'SHALLOW' ? (
    +                                         <i
    +                                             className=\"icon-eye interests-watch\"
    +                                             onClick={() => {
    +                                                 watchObject('YES', item);
    +                                             }}>
    +                                             {item.interest.watching == 'YES' || item.interest.watching == 'SHALLOW' ? (
    +                                                 <i
    +                                                     className=\"icon-eye interests-watch active\"
    +                                                     onClick={() => {
    +                                                         watchObject('ENO', item);
    +                                                     }}
    +                                                     title=\"You are watching this item\"
    +                                                 />
    +                                             ) : null}
    +                                         </i>
    +                                     ) : null}
    +                                 </i>
    +                             ) : null}
    +                         </i>
    +                     ) : null}
    +                 </div>
    +                 {!item.interest.starred ? (
    +                     <i
    +                         className=\"icon-star interests-star\"
    +                         onClick={() => {
    +                             starObject(true, item);
    +                         }}>
    +                         {item.interest.starred ? (
    +                             <i
    +                                 className=\"icon-star interests-star active\"
    +                                 onClick={() => {
    +                                     starObject(false, item);
    +                                 }}
    +                                 title=\"You have starred this item\">
    +                                 {item.interest.watching != 'YES' && item.interest.watching != 'SHALLOW' ? (
    +                                     <i
    +                                         className=\"icon-eye interests-watch\"
    +                                         onClick={() => {
    +                                             watchObject('YES', item);
    +                                         }}>
    +                                         {item.interest.watching == 'YES' || item.interest.watching == 'SHALLOW' ? (
    +                                             <i
    +                                                 className=\"icon-eye interests-watch active\"
    +                                                 onClick={() => {
    +                                                     watchObject('ENO', item);
    +                                                 }}
    +                                                 title=\"You are watching this item\">
    +                                                 {/* RIGHT COLUMN PREVIEW */}
    +                                                 <div data-block=\"preview\" right-column-tab=\"details\">
    +                                                     <div analysis-right-column-summary=\"\" />
    +                                                 </div>
    +                                             </i>
    +                                         ) : null}
    +                                     </i>
    +                                 ) : null}
    +                             </i>
    +                         ) : null}
    +                     </i>
    +                 ) : null}
    +             </div>
    +             {!item.interest.starred ? (
    +                 <i
    +                     className=\"icon-star interests-star\"
    +                     onClick={() => {
    +                         starObject(true, item);
    +                     }}>
    +                     {item.interest.starred ? (
    +                         <i
    +                             className=\"icon-star interests-star active\"
    +                             onClick={() => {
    +                                 starObject(false, item);
    +                             }}
    +                             title=\"You have starred this item\">
    +                             {item.interest.watching != 'YES' && item.interest.watching != 'SHALLOW' ? (
    +                                 <i
    +                                     className=\"icon-eye interests-watch\"
    +                                     onClick={() => {
    +                                         watchObject('YES', item);
    +                                     }}>
    +                                     {item.interest.watching == 'YES' || item.interest.watching == 'SHALLOW' ? (
    +                                         <i
    +                                             className=\"icon-eye interests-watch active\"
    +                                             onClick={() => {
    +                                                 watchObject('ENO', item);
    +                                             }}
    +                                             title=\"You are watching this item\"
    +                                         />
    +                                     ) : null}
    +                                 </i>
    +                             ) : null}
    +                         </i>
    +                     ) : null}
    +                 </i>
    +             ) : null}
    +         </div>,
    +         !item.interest.starred ? (
    +             <i
    +                 key=\"child-1\"
    +                 className=\"icon-star interests-star\"
    +                 onClick={() => {
    +                     starObject(true, item);
    +                 }}>
    +                 {item.interest.starred ? (
    +                     <i
    +                         className=\"icon-star interests-star active\"
    +                         onClick={() => {
    +                             starObject(false, item);
    +                         }}
    +                         title=\"You have starred this item\">
    +                         {item.interest.watching != 'YES' && item.interest.watching != 'SHALLOW' ? (
    +                             <i
    +                                 className=\"icon-eye interests-watch\"
    +                                 onClick={() => {
    +                                     watchObject('YES', item);
    +                                 }}>
    +                                 {item.interest.watching == 'YES' || item.interest.watching == 'SHALLOW' ? (
    +                                     <i
    +                                         className=\"icon-eye interests-watch active\"
    +                                         onClick={() => {
    +                                             watchObject('ENO', item);
    +                                         }}
    +                                         title=\"You are watching this item\"
    +                                     />
    +                                 ) : null}
    +                             </i>
    +                         ) : null}
    +                     </i>
    +                 ) : null}
    +             </i>
    +         ) : null
    +     ];
    + };
    + 
    + export default TestComponent;