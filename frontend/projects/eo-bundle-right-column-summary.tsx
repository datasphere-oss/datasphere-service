import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"eo-bundle-right-column-summary right-column-summary object-right-column-summary vertical-flex h100\">
    +             <div
    +                 item-header=\"\"
    +                 className=\"column-header noflex\"
    +                 href=\"getItemHeaderHref(rightColumnItem)\"
    +                 color=\"shared-objects\"
    +                 icon=\"icon-dku-share\"
    +                 title=\"共享对象\">
    +                 共享对象
    +             </div>
    + 
    +             <div className=\"exposed-objects-wrapper flex oa\">
    +                 {/* from project to project */}
    +                 <div className=\"from-to-wrapper column-panel accordion\">
    +                     <ul>
    +                         <li>
    +                             <span className=\"field-name\">从:</span>
    +                             <CropedProjectPicture
    +                                 project-key=\"rightColumnItem.fromProjectKey\"
    +                                 object-type=\"'PROJECT'\"
    +                                 object-id=\"rightColumnItem.fromProjectKey\"
    +                                 object-img-hash=\"projectsMap[rightColumnItem.fromProjectKey].objectImgHash\"
    +                                 size-x=\"80\"
    +                                 size-y=\"170\"
    +                             />
    +                             <span className=\"project-name\">{projectsMap[rightColumnItem.fromProjectKey].name}</span>
    +                         </li>
    +                         <li>
    +                             <span className=\"field-name\">到:</span>
    +                             <CropedProjectPicture
    +                                 project-key=\"rightColumnItem.toProjectKey\"
    +                                 object-type=\"'PROJECT'\"
    +                                 object-id=\"rightColumnItem.toProjectKey\"
    +                                 object-img-hash=\"projectsMap[rightColumnItem.toProjectKey].objectImgHash\"
    +                                 size-x=\"80\"
    +                                 size-y=\"170\"
    +                             />
    +                             <span className=\"project-name\">{projectsMap[rightColumnItem.toProjectKey].name}</span>
    +                         </li>
    +                     </ul>
    +                 </div>
    + 
    +                 {/* Datasets */}
    +                 {rightColumnItem.exposedDatasets.length ? (
    +                     <div className=\"exposed-datasets-wrapper column-panel accordion\">
    +                         <span ng-init=\"show = true\">
    +                             <h4
    +                                 className=\"accordion-title\"
    +                                 onClick={() => {
    +                                     show = !show;
    +                                 }}>
    +                                 <i className=\"{'icon-chevron-up':show, 'icon-chevron-down':!show}\" />
    +                                 数据集 ({rightColumnItem.exposedDatasets.length})
    +                             </h4>
    +                             {show ? (
    +                                 <ul>
    +                                     {rightColumnItem.exposedDatasets.map((dataset, index: number) => {
    +                                         return (
    +                                             <li key={`item-${index}`}>
    +                                                 <i className={`type ${datasetTypeToIcon(dataset.type)}`} />
    +                                                 <a
    +                                                     onClick={(event: React.SyntheticEvent<HTMLElement>) => {
    +                                                         rightColumnItem.canReadFromProject &&
    +                                                             goToOriginal(dataset, event);
    +                                                     }}
    +                                                     className=\"{'disabled' : !rightColumnItem.canReadFromProject}\">
    +                                                     {dataset.name}
    +                                                 </a>
    +                                                 {/* <i class=\"go-to-btn icon icon-reply\"
    +    ng-class=\"{'disabled' : !rightColumnItem.canReadFromProject}\"
    +    ng-click=\"rightColumnItem.canReadFromProject && goToOriginal(dataset, $event)\">
    +                     </i> */}
    +                                                 <i
    +                                                     className=\"{'disabled' : !rightColumnItem.canReadToProject}\"
    +                                                     onClick={(event: React.SyntheticEvent<HTMLElement>) => {
    +                                                         rightColumnItem.canReadToProject && goToForeign(dataset, event);
    +                                                     }}
    +                                                     title=\"open in target project\"
    +                                                     toggle=\"tooltip\"
    +                                                 />
    +                                             </li>
    +                                         );
    +                                     })}
    +                                 </ul>
    +                             ) : null}
    +                         </span>
    +                     </div>
    +                 ) : null}
    + 
    +                 {/* Models */}
    +                 {rightColumnItem.exposedModels.length ? (
    +                     <div className=\"exposed-models-wrapper column-panel accordion\">
    +                         <span ng-init=\"show = true\">
    +                             <h4
    +                                 className=\"accordion-title\"
    +                                 onClick={() => {
    +                                     show = !show;
    +                                 }}>
    +                                 <i className=\"{'icon-chevron-up':show, 'icon-chevron-down':!show}\" />
    +                                 模型 ({rightColumnItem.exposedModels.length})
    +                             </h4>
    +                             {show ? (
    +                                 <ul>
    +                                     {rightColumnItem.exposedModels.map((model, index: number) => {
    +                                         return (
    +                                             <li key={`item-${index}`}>
    +                                                 <i className={`type ${modelTypeToIcon(model.type)}`} />
    +                                                 <a
    +                                                     onClick={(event: React.SyntheticEvent<HTMLElement>) => {
    +                                                         rightColumnItem.canReadFromProject &&
    +                                                             goToOriginal(model, event);
    +                                                     }}
    +                                                     className=\"{'disabled' : !rightColumnItem.canReadFromProject}\">
    +                                                     {model.name}
    +                                                 </a>
    +                                                 {/* <i class=\"go-to-btn icon icon-reply\"
    +    ng-class=\"{'disabled' : !rightColumnItem.canReadFromProject}\"
    +    ng-click=\"rightColumnItem.canReadFromProject && goToOriginal(model, $event)\">
    +                     </i> */}
    +                                             </li>
    +                                         );
    +                                     })}
    +                                 </ul>
    +                             ) : null}
    +                         </span>
    +                     </div>
    +                 ) : null}
    + 
    +                 {/* Managed Folders */}
    +                 {rightColumnItem.exposedFolders.length ? (
    +                     <div className=\"exposed-folders-wrapper column-panel accordion\">
    +                         <span ng-init=\"show = true\">
    +                             <h4
    +                                 className=\"accordion-title\"
    +                                 onClick={() => {
    +                                     show = !show;
    +                                 }}>
    +                                 <i className=\"{'icon-chevron-up':show, 'icon-chevron-down':!show}\" />
    +                                 托管文件夹 ({rightColumnItem.exposedFolders.length})
    +                             </h4>
    +                             {show ? (
    +                                 <ul>
    +                                     {rightColumnItem.exposedFolders.map((folder, index: number) => {
    +                                         return (
    +                                             <li key={`item-${index}`}>
    +                                                 <i className=\"type icon-folder-open\" />
    +                                                 <a
    +                                                     onClick={(event: React.SyntheticEvent<HTMLElement>) => {
    +                                                         rightColumnItem.canReadFromProject &&
    +                                                             goToOriginal(folder, event);
    +                                                     }}
    +                                                     className=\"{'disabled' : !rightColumnItem.canReadFromProject}\">
    +                                                     {folder.name}
    +                                                 </a>
    +                                                 {/* <i class=\"go-to-btn icon icon-reply\"
    +    ng-class=\"{'disabled' : !rightColumnItem.canReadFromProject}\"
    +    ng-click=\"rightColumnItem.canReadFromProject && goToOriginal(folder, $event)\">
    +                     </i> */}
    +                                             </li>
    +                                         );
    +                                     })}
    +                                 </ul>
    +                             ) : null}
    +                         </span>
    +                     </div>
    +                 ) : null}
    + 
    +                 {/* Notebook */}
    +                 {rightColumnItem.exposedNotebooks.length ? (
    +                     <div className=\"exposed-notebooks-wrapper column-panel accordion\">
    +                         <span ng-init=\"show = true\">
    +                             <h4
    +                                 className=\"accordion-title\"
    +                                 onClick={() => {
    +                                     show = !show;
    +                                 }}>
    +                                 <i className=\"{'icon-chevron-up':show, 'icon-chevron-down':!show}\" />
    +                                 Notebooks ({rightColumnItem.exposedNotebooks.length})
    +                             </h4>
    +                             {show ? (
    +                                 <ul>
    +                                     {rightColumnItem.exposedNotebooks.map((notebook, index: number) => {
    +                                         return (
    +                                             <li key={`item-${index}`}>
    +                                                 <i className=\"type icon-dku-nav_notebook\" />
    +                                                 <a
    +                                                     onClick={(event: React.SyntheticEvent<HTMLElement>) => {
    +                                                         rightColumnItem.canReadFromProject &&
    +                                                             goToOriginal(notebook, event);
    +                                                     }}
    +                                                     className=\"{'disabled' : !rightColumnItem.canReadFromProject}\">
    +                                                     {notebook.name}
    +                                                 </a>
    +                                                 {/* <i class=\"go-to-btn icon icon-reply\"
    +    ng-class=\"{'disabled' : !rightColumnItem.canReadFromProject}\"
    +    ng-click=\"rightColumnItem.canReadFromProject && goToOriginal(folder, $event)\">
    +                     </i> */}
    +                                             </li>
    +                                         );
    +                                     })}
    +                                 </ul>
    +                             ) : null}
    +                         </span>
    +                     </div>
    +                 ) : null}
    + 
    +                 {/* Webapp */}
    +                 {rightColumnItem.exposedWebapp.length ? (
    +                     <div className=\"exposed-webapps-wrapper column-panel accordion\">
    +                         <span ng-init=\"show = true\">
    +                             <h4
    +                                 className=\"accordion-title\"
    +                                 onClick={() => {
    +                                     show = !show;
    +                                 }}>
    +                                 <i className=\"{'icon-chevron-up':show, 'icon-chevron-down':!show}\" />
    +                                 Web 应用 ({rightColumnItem.exposedWebapp.length})
    +                             </h4>
    +                             {show ? (
    +                                 <ul>
    +                                     {rightColumnItem.exposedWebapp.map((webapp, index: number) => {
    +                                         return (
    +                                             <li key={`item-${index}`}>
    +                                                 <i className=\"type icon-code\" />
    +                                                 <a
    +                                                     onClick={(event: React.SyntheticEvent<HTMLElement>) => {
    +                                                         rightColumnItem.canReadFromProject &&
    +                                                             goToOriginal(webapp, event);
    +                                                     }}
    +                                                     className=\"{'disabled' : !rightColumnItem.canReadFromProject}\">
    +                                                     {webapp.name}
    +                                                 </a>
    +                                                 {/* <i class=\"go-to-btn icon icon-reply\"
    +    ng-class=\"{'disabled' : !rightColumnItem.canReadFromProject}\"
    +    ng-click=\"rightColumnItem.canReadFromProject && goToOriginal(webapp, $event)\">
    +                     </i> */}
    +                                             </li>
    +                                         );
    +                                     })}
    +                                 </ul>
    +                             ) : null}
    +                         </span>
    +                     </div>
    +                 ) : null}
    + 
    +                 {/* Report */}
    +                 {rightColumnItem.exposedReports.length ? (
    +                     <div className=\"exposed-reports-wrapper column-panel accordion\">
    +                         <span ng-init=\"show = true\">
    +                             <h4
    +                                 className=\"accordion-title\"
    +                                 onClick={() => {
    +                                     show = !show;
    +                                 }}>
    +                                 <i className=\"{'icon-chevron-up':show, 'icon-chevron-down':!show}\" />
    +                                 报表 ({rightColumnItem.exposedWebapp.length})
    +                             </h4>
    +                             {show ? (
    +                                 <ul>
    +                                     {rightColumnItem.exposedReports.map((report, index: number) => {
    +                                         return (
    +                                             <li key={`item-${index}`}>
    +                                                 <i className={`type ${typeToIcon('report')}`} />
    +                                                 <a
    +                                                     onClick={(event: React.SyntheticEvent<HTMLElement>) => {
    +                                                         rightColumnItem.canReadFromProject &&
    +                                                             goToOriginal(report, event);
    +                                                     }}
    +                                                     className=\"{'disabled' : !rightColumnItem.canReadFromProject}\">
    +                                                     {report.name}
    +                                                 </a>
    +                                                 {/* <i class=\"go-to-btn icon icon-reply\"
    +    ng-class=\"{'disabled' : !rightColumnItem.canReadFromProject}\"
    +    ng-click=\"rightColumnItem.canReadFromProject && goToOriginal(report, $event)\">
    +                     </i> */}
    +                                             </li>
    +                                         );
    +                                     })}
    +                                 </ul>
    +                             ) : null}
    +                         </span>
    +                     </div>
    +                 ) : null}
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;