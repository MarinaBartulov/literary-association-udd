import { useParams, useHistory } from "react-router-dom";
import { useEffect, useState } from "react";
import Header from "./Header";

import { boardMemberService } from "../services/board-member-service";

import Card from "react-bootstrap/Card";
import ListGroup from "react-bootstrap/ListGroup";
import Button from "react-bootstrap/Button";
import Table from "react-bootstrap/Table";
import { toast } from "react-toastify";

const MembershipApplication = () => {
  const { taskId, processId } = useParams();
  const [membershipApplication, setMembershipApplication] = useState({});
  const [literaryWorks, setLiteraryWorks] = useState([]);
  const history = useHistory();

  useEffect(() => {
    getMembershipApplication();
  }, []);

  const getMembershipApplication = async () => {
    try {
      const response = await boardMemberService.getMembershipApplication(
        processId
      );
      setMembershipApplication(response);
      setLiteraryWorks(response.literaryWorks);
    } catch (error) {
      if (error.response) {
        console.log("Error: " + JSON.stringify(error.response));
      }
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };

  return (
    <div>
      <Header />
      <Card
        body
        style={{
          width: "40rem",
          margin: "auto",
          marginTop: "2rem",
          marginBottom: "2rem",
          backgroundColor: "rgb(70, 130, 180)",
        }}
      >
        <h2>Membership Application</h2>
        <ListGroup>
          <ListGroup.Item>Id: #{membershipApplication.id}</ListGroup.Item>
          <ListGroup.Item>
            Writers Firstname: {membershipApplication.writerFirstName}
          </ListGroup.Item>
          <ListGroup.Item>
            Writers Surname: {membershipApplication.writerLastName}
          </ListGroup.Item>
          <ListGroup.Item>
            Price: {membershipApplication.price} $
          </ListGroup.Item>
        </ListGroup>
        <br />
        <h3>Literary Works:</h3>
        <Table striped bordered hover variant="light">
          <thead>
            <tr>
              <th>Title</th>
              <th>Download</th>
            </tr>
          </thead>
          <tbody>
            {literaryWorks.map((literaryWork) => {
              return (
                <tr key={literaryWork.id}>
                  <td>{literaryWork.title}</td>
                  <td>
                    <Button
                      variant="dark"
                      onClick={() => {
                        window.open(literaryWork.downloadUrl);
                      }}
                    >
                      Download
                    </Button>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </Table>
        <Button
          variant="dark"
          onClick={() => {
            history.push("/giveOpinion/" + processId + "/" + taskId);
          }}
        >
          Give opinion
        </Button>
      </Card>
    </div>
  );
};

export default MembershipApplication;
