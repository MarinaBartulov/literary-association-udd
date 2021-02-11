import React, { useState, useEffect } from "react";
import { useHistory } from "react-router-dom";
import Header from "./Header";
import { taskService } from "../services/task-service";

import Button from "react-bootstrap/Button";
import { Table } from "react-bootstrap";
import { toast } from "react-toastify";

const BoardMemberPanel = () => {
  const [tasks, setTasks] = useState([]);
  const history = useHistory();

  useEffect(() => {
    getAllTasks();
  }, []);

  const getAllTasks = async () => {
    try {
      const response = await taskService.getAllTasks();
      console.log(response);
      setTasks(response);
    } catch (error) {
      if (error.response) {
        console.log("Error: " + JSON.stringify(error.response));
      }
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };

  const goToMembershipApplication = (taskId, processId) => {
    console.log(taskId);
    console.log(processId);
    history.push("/membershipApplication/" + taskId + "/" + processId);
  };

  return (
    <div>
      <Header />
      <h2> Membership Applications </h2>
      <div
        style={{ width: "70%", backgroundColor: "#bdbbbb" }}
        className="ml-auto mr-auto"
      >
        <Table>
          <thead>
            <tr>
              <th>#</th>
              <th>Task</th>
              <th>Task ID</th>
              <th>See details</th>
            </tr>
          </thead>
          <tbody>
            {tasks.map((task, i) => {
              return (
                <tr key={i}>
                  <td>{i + 1}</td>
                  <td>{task.name}</td>
                  <td>{task.taskId}</td>
                  <td>
                    <Button
                      variant="dark"
                      onClick={() => {
                        goToMembershipApplication(task.taskId, task.processId);
                      }}
                    >
                      Details
                    </Button>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </Table>
      </div>
    </div>
  );
};

export default BoardMemberPanel;
