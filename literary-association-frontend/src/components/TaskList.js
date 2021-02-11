import React, { useEffect, useState } from "react";
import { taskService } from "../services/task-service";
import Header from "./Header";
import { toast } from "react-toastify";
import Button from "react-bootstrap/Button";
import { Table } from "react-bootstrap";
import { useHistory } from "react-router-dom";

const TaskList = () => {
  const [tasks, setTasks] = useState([]);
  const history = useHistory();

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

  const taskDetails = (taskId) => {
    history.push("/task/" + taskId);
  };

  useEffect(() => {
    getAllTasks();
  }, []);

  return (
    <div>
      <Header />
      <h2>All my tasks</h2>
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
                        taskDetails(task.taskId);
                      }}
                    >
                      See details
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

export default TaskList;
