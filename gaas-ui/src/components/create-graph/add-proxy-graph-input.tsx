import React, {ReactElement} from "react";
import {Button, FormHelperText, Grid, InputLabel, TextField} from "@material-ui/core";
import AddCircleOutlineOutlinedIcon from "@material-ui/icons/AddCircleOutlineOutlined";
import {Graph} from "../../domain/graph";
import {GraphType} from "../../domain/graph-type";
import {StoreType} from "../../domain/store-type";
import {AlertType, INotificationAlertProps} from "../alerts/notification-alert";
import {GetGraphStatusRepo} from "../../rest/repositories/get-graph-status-repo";
import {GetGraphDetailsRepo} from "../../rest/repositories/get-graph-details-repo";

interface IProps {
    hide: boolean;
    onChangeProxyURL (proxyURL: string): void;
    proxyURLValue: string;
    onClickAddProxyGraph (newProxyGraph: Graph,alert:INotificationAlertProps  ): void;
}

export default function AddProxyGraphInput(props: IProps): ReactElement {
    const {
        hide,
        onChangeProxyURL,
        proxyURLValue,
        onClickAddProxyGraph,
    }= props;

    async function checkSubmit(){
        try{
            const status: string = await new GetGraphStatusRepo().getStatus(proxyURLValue);
            if(status === "UP"){
                const description: string = await new GetGraphDetailsRepo().getDescription(proxyURLValue);
                const graph: Graph = new Graph(proxyURLValue + "-graph", description, proxyURLValue, status, StoreType.PROXY_STORE, GraphType.PROXY_GRAPH);
                onClickAddProxyGraph(graph,{alertType: AlertType.SUCCESS, message: "Graph is valid"});
                onChangeProxyURL("");
            }else{
                onClickAddProxyGraph(new Graph("","","","", StoreType.PROXY_STORE, GraphType.PROXY_GRAPH),{alertType: AlertType.FAILED, message: "Graph status is DOWN so could not be added"});
            }
        }catch(e){
            onClickAddProxyGraph(new Graph("","","","", StoreType.PROXY_STORE, GraphType.PROXY_GRAPH),{alertType: AlertType.FAILED, message: `Graph is invalid ${e.toString()}`});

        }
    }

      function isValidHttpUrl(string: string) {
        let url;
        
        try {
          url = new URL(string);
        } catch (_) {
          return false;  
        }
      
        return url.protocol === "http:" || url.protocol === "https:";
      }

    return (
        <>
        {!hide &&
        <div id={"graphs-table"}>
            <Grid item xs={12} id={"proxy-url-grid"}>
                <InputLabel aria-label="proxy-url-input-label">Proxy URL</InputLabel>

                <TextField
                    id="proxy-url"
                    aria-label="proxy-url-textfield"
                    inputProps={{
                        name: "Proxy URL",
                        id: "proxy-url-input",
                        "aria-label": "proxy-url-input"
                    }}
                    variant="outlined"
                    value={proxyURLValue}
                    fullWidth
                    name="proxy-url"
                    error={!isValidHttpUrl(proxyURLValue)}
                    onChange={(event) => {
                        onChangeProxyURL(event.target.value)
                    }}
                />
                <FormHelperText aria-label="proxy-url-form-helper-text">
                    Enter valid URL for proxy store if not shown below in table
                </FormHelperText>
            </Grid>
            <Grid
                id="proxy-button-grid"
                container
                style={{margin: 10}}
                direction="row"
                justify="center"
                alignItems="center"
                aria-label="proxy-url-button-grid"
            >
                <Button
                    aria-label="proxy-url-submit-button"
                    id="add-new-proxy-button"
                    onClick={async () => await checkSubmit()}
                    startIcon={<AddCircleOutlineOutlinedIcon/>}
                    type="submit"
                    variant="contained"
                    color="primary"
                    disabled={proxyURLValue === "" || !isValidHttpUrl(proxyURLValue)}
                >
                    Add Proxy Graph
                </Button>
            </Grid>
        </div>}
        </>
    );

}