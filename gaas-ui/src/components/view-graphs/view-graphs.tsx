import {
    Button,
    Container,
    Grid,
    Table,
    Typography,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Toolbar,
} from "@material-ui/core";
import Box from "@material-ui/core/Box";
import CardContent from "@material-ui/core/CardContent";
import Paper from "@material-ui/core/Paper";
import { makeStyles } from "@material-ui/core/styles";
import RefreshOutlinedIcon from "@material-ui/icons/RefreshOutlined";
import React from "react";
import { Graph } from "../../domain/graph";
import { StoreType } from "../../domain/store-type";
import { DeleteGraphRepo } from "../../rest/repositories/delete-graph-repo";
import { GetAllGraphsRepo } from "../../rest/repositories/get-all-graphs-repo";
import { AlertType, NotificationAlert } from "../alerts/notification-alert";
import { Copyright } from "../copyright/copyright";
import Gauge from "./gauge";
import { MainGraphTableRow } from "./main-graph-table-row";

interface IState {
    graphs: Graph[];
    selectedRow: any;
    errorMessage: string;
}

export default class ViewGraph extends React.Component<{}, IState> {
    constructor(props: Object) {
        super(props);
        this.state = {
            graphs: [],
            selectedRow: "",
            errorMessage: "",
        };
    }

    public async componentDidMount() {
        this.getGraphs();
    }

    private async getGraphs() {
        try {
            const graphs: Graph[] = await new GetAllGraphsRepo().getAll();
            this.setState({ graphs, errorMessage: "" });
        } catch (e) {
            this.setState({ errorMessage: `Failed to get all graphs. ${e.toString()}` });
        }
    }

    private async deleteGraph(graphName: string) {
        try {
            await new DeleteGraphRepo().delete(graphName);
            await this.getGraphs();
        } catch (e) {
            this.setState({ errorMessage: `Failed to delete graph "${graphName}". ${e.toString()}` });
        }
    }

    private classes: any = makeStyles({
        root: {
            width: "100%",
            marginTop: 40,
        },
        table: {
            minWidth: 650,
        },
    });

    public render() {
        const { graphs, errorMessage } = this.state;

        return (
            <main aria-label={"view-graphs-page"}>
                {errorMessage && <NotificationAlert alertType={AlertType.FAILED} message={errorMessage} />}
                <Toolbar />
                    <Container maxWidth="md">
                        <Box my={2}>
                            <Typography variant="h4" align={"center"} id={"view-graphs-title"} aria-label={"view-graphs-title"}>
                                View Graphs
                            </Typography>
                        </Box>
                        <Grid container spacing={3} >

                            <Grid item xs={6} justify="center"
                                  alignItems="center">
                                <Paper>
                                    <CardContent>
                                        <Typography gutterBottom variant="h6" component="h2">
                                            Summary
                                        </Typography>
                                        <Gauge
                                            maxValue={graphs.length}
                                            data={[
                                                { key: "TOTAL", data: graphs.length },
                                                { key: "UP", data: graphs.filter((graph) => graph.getStatus() === "UP").length },
                                                { key: "DOWN", data: graphs.filter((graph) => graph.getStatus() === "DOWN").length },
                                            ]}
                                            colours={[
                                                "#fdb81e",
                                                "#00ECB1",
                                                "#F50057",
                                            ]}
                                        />
                                    </CardContent>
                                </Paper>
                            </Grid>

                            <Grid item xs={6}>
                                <Paper>
                                        <CardContent>
                                            <Typography gutterBottom variant="h6" component="h2">
                                                Store Types
                                            </Typography>
                                            <Gauge 
                                                maxValue={graphs.length}
                                                data={[
                                                    { key: "ACCUMULO", data: graphs.filter((graph) => graph.getStoreType() === StoreType.ACCUMULO).length },
                                                    { key: "FEDERATED", data: graphs.filter((graph) => graph.getStoreType() === StoreType.FEDERATED_STORE).length },
                                                    { key: "MAP", data: graphs.filter((graph) => graph.getStoreType() === StoreType.MAPSTORE).length },
                                                ]}
                                                colours={[
                                                    "#02bfe7",
                                                    "#02bfe7",
                                                    "#02bfe7",
                                                ]} 
                                            />
                                        </CardContent>
                                </Paper>
                            </Grid>

                            <Grid item xs={12}>
                            <TableContainer component={Paper}>
                                <Table size="medium" className={this.classes.table} aria-label="Graphs Table">
                                    <TableHead>
                                        <TableRow style={{ background: "#F4F2F2" }}>
                                            <TableCell />
                                            <TableCell>Graph ID</TableCell>
                                            <TableCell>Store Type</TableCell>
                                            <TableCell>Status</TableCell>
                                            <TableCell>URL</TableCell>
                                            <TableCell>Actions</TableCell>
                                        </TableRow>
                                    </TableHead>

                                    <TableBody>
                                        {graphs.map((graph: Graph, index) => (
                                            <MainGraphTableRow 
                                                key={graph.getId()} 
                                                index={index} row={graph} 
                                                onClickDelete={(graphId: string) => this.deleteGraph(graphId)} />
                                        ))}
                                    </TableBody>
                                    {graphs.length === 0 && <caption>No Graphs.</caption>}
                                </Table>
                            </TableContainer>
                        
                            <Grid container style={{ margin: 10 }} direction="row" justify="center" alignItems="center">
                                <Button
                                    id="view-graphs-refresh-button"
                                    onClick={async () => await this.getGraphs()}
                                    startIcon={<RefreshOutlinedIcon />}
                                    variant="contained"
                                    color="primary"
                                    className={this.classes.submit}
                                >
                                    Refresh Table
                                </Button>
                            </Grid>
                        </Grid>
                    </Grid>
                    <Box pt={4}>
                        <Copyright />
                    </Box>
                </Container>
            </main>
        );
    }
}
